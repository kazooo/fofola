package cz.mzk.fofola.process.solr_response;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.constants.dnnt.BasicLabel;
import cz.mzk.fofola.constants.dnnt.Label;
import cz.mzk.fofola.constants.solr.ModelName;
import cz.mzk.fofola.constants.solr.SolrField;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import cz.mzk.fofola.service.FileService;
import cz.mzk.fofola.service.SolrService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolrResponseWritingProcess extends Process {

    private final String solrHost;
    private final String model;
    private final String accessibility;
    private final String yearFrom;
    private final String yearTo;
    private final String dnntLabel;
    private final String dnntFlag;
    private final String field;
    private final String searchLink;

    public static final String ANY_FIELD_VALUE = "any";
    public static final String NO_FIELD_VALUE = "none";

    private static final Map<String, String> CLIENT_SOLR_FIELD_MAPPING = new HashMap<>() {{
        put("title", SolrField.TITLE);
        put("licences", SolrField.DNNT_LABELS);
        put("authors", SolrField.AUTHOR);
        put("locations", SolrField.PHYSICAL_LOCATION);
        put("languages", SolrField.LANGUAGE);
        put("doctypes", SolrField.MODEL);
        put("geonames", SolrField.GEO_NAMES);
        put("accessibility", SolrField.ACCESSIBILITY);
        put("collections", SolrField.COLLECTION);
    }};

    public SolrResponseWritingProcess(ProcessParams params) throws IOException {
        super(params);

        final FofolaConfiguration fofolaConfig = params.getConfig();
        final Map<String, Object> data = params.getData();

        yearFrom = (String) data.get("yearFrom");
        yearTo = (String) data.get("yearTo");
        model = (String) data.getOrDefault("model", ANY_FIELD_VALUE);
        accessibility = (String) data.getOrDefault("access", ANY_FIELD_VALUE);
        dnntFlag = (String) data.getOrDefault("dnntFlag", ANY_FIELD_VALUE);
        field = (String) data.getOrDefault("field", null);
        searchLink = (String) data.getOrDefault("searchLink", null);

        final String dnntLabelRaw = (String) data.getOrDefault("dnntLabel", ANY_FIELD_VALUE);
        final Label label = BasicLabel.of(dnntLabelRaw);
        if (label != null) {
            this.dnntLabel = label.getValue();
        } else {
            this.dnntLabel = dnntLabelRaw;
        }

        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public TerminationReason process() throws Exception {
        final SolrClient solrClient = SolrService.buildClient(solrHost);
        final String readyOutFileName = FileService.fileNameWithDateStampPrefix("solr-response.txt");
        final File notReadyOutFile = FileService.getSolrRespOutputFile("not-ready-" + readyOutFileName);
        final PrintWriter output = new PrintWriter(notReadyOutFile);
        final SolrQuery params = createSolrQuery();

        final Consumer<SolrDocument> logic = solrDoc -> {
            final String fieldValue = (String) solrDoc.getFieldValue(field);
            output.println(fieldValue);
        };

        try {
            SolrService.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                    params,
                    solrClient,
                    logic,
                    5000
            );
        } catch (Exception e) {
            output.close();
            renameOutFile(notReadyOutFile, "terminated-" + readyOutFileName);
            throw e;
        }

        output.flush();
        output.close();
        solrClient.close();
        renameOutFile(notReadyOutFile, readyOutFileName);
        return null;
    }

    private SolrQuery createSolrQuery() {
        if (searchLink != null) {
            return createQueryFromLink();
        } else {
            return createQueryFromFields();
        }
    }

    private SolrQuery createQueryFromFields() {
        final List<String> queryParts = new ArrayList<>();

        if (model != null) {
            queryParts.add(wrapFieldQuery(SolrField.MODEL, model));
        }

        if (accessibility != null) {
            queryParts.add(wrapFieldQuery(SolrField.ACCESSIBILITY, accessibility));

        }

        if (dnntFlag != null) {
            queryParts.add(wrapFieldQuery(SolrField.DNNT_FLAG, dnntFlag));
        }

        if (dnntLabel != null) {
            queryParts.add(wrapFieldQuery(SolrField.DNNT_LABELS, dnntLabel));
        }

        if (yearTo != null || yearFrom != null) {
            queryParts.add(wrapDateRange(SolrField.YEAR_FIELD_NAME, getYearRange(yearFrom, yearTo)));
        }

        final String query = String.join(" AND ", queryParts);
        logger.info(query);

        final SolrQuery queryParams = new SolrQuery(query);
        queryParams.addField(field);

        return queryParams;
    }

    private SolrQuery createQueryFromLink() {
        final List<String> filterQueryParts = new ArrayList<>() {{
            add(wrapPart(createSearchLinkModelList()));
        }};

        final Map<String, String> linkQueryParts = extractQueryFields(searchLink);
        final String query = createQuery(linkQueryParts);

        final List<String> convertedLinkQueryParts  = convertLinkQueryParts(linkQueryParts);
        filterQueryParts.addAll(convertedLinkQueryParts.stream().map(this::wrapPart).toList());

        final String filterQuery = String.join(" AND ", filterQueryParts);
        logger.info("Query: " + query);
        logger.info("Filter Query: " + filterQuery);

        final SolrQuery queryParams = new SolrQuery(query);
        queryParams.addFilterQuery(filterQuery);
        queryParams.addField(field);

        return queryParams;
    }

    private String wrapFieldQuery(final String fieldName, final String fieldValue) {
        if (ANY_FIELD_VALUE.equals(fieldValue)) {
            return fieldName + ":*";
        } else if (NO_FIELD_VALUE.equals(fieldValue)) {
            return "-" + fieldName + ":*";
        } else {
            return fieldName + ":\"" + fieldValue + "\"";
        }
    }

    private String wrapDateRange(final String fieldName, final String fieldValue) {
        if (ANY_FIELD_VALUE.equals(fieldValue)) {
            return fieldName + ":[* TO *]";
        } else if (NO_FIELD_VALUE.equals(fieldValue)) {
            return "-" + fieldName + ":[* TO *]";
        } else {
            return fieldName + ":" + fieldValue;
        }
    }

    private String wrapPart(final String queryPart) {
        return "(" + queryPart + ")";
    }

    private String getYearRange(final String yearFrom, final String yearTo) {
        String range = "[";
        if (yearFrom != null)
            range += yearFrom + " TO ";
        else
            range += "* TO ";
        if (yearTo != null)
            range += yearTo + "]";
        else
            range += "*]";
        return range;
    }

    private String createSearchLinkModelList() {
        return Stream.of(
                    ModelName.MONOGRAPH,
                    ModelName.PERIODICAL,
                    ModelName.GRAPHIC,
                    ModelName.MAP,
                    ModelName.SHEETMUSIC,
                    ModelName.SOUNDRECORDING,
                    ModelName.ARCHIVE,
                    ModelName.MANUSCRIPT,
                    ModelName.MONOGRAPHUNIT
                )
                .map(model -> wrapFieldQuery(SolrField.MODEL, model))
                .collect(Collectors.joining(" OR "));
    }

    private Map<String, String> extractQueryFields(final String searchLink) {
        final Map<String, String> queryParts = new HashMap<>();
        final String queryLinkPart = searchLink.substring(searchLink.indexOf("?") + 1);
        Arrays.stream(queryLinkPart.split("&"))
                .forEach(fieldValue -> {
                    final String[] parts = fieldValue.split("=");
                    queryParts.put(parts[0], parts[1]);
                });
        return queryParts;
    }

    private List<String> convertLinkQueryParts(final Map<String, String> queryParts) {
        final List<String> result = new ArrayList<>();
        /* map client Solr field names */
        CLIENT_SOLR_FIELD_MAPPING.forEach((clientKey, solrKey) -> {
            if (queryParts.containsKey(clientKey)) {
                String value = queryParts.get(clientKey);
                if (clientKey.equals("doctypes") && value.equals(ModelName.MONOGRAPH)) {
                    /* add ',,' to process as an array with additional model type */
                    value += ",,monographunit";
                }
                result.add(handlePossibleArray(solrKey, value));
                queryParts.remove(clientKey);
            }
        });
        /* handle date range */
        if (queryParts.containsKey("from") || queryParts.containsKey("to")) {
            result.add(wrapDateRange(queryParts.get("from"), queryParts.get("to")));
            queryParts.remove("from");
            queryParts.remove("to");
        }
        /* wrap and add the rest of incoming query parts */
        queryParts.forEach((key, value) -> result.add(handlePossibleArray(key, value)));
        return result;
    }

    private String handlePossibleArray(final String fieldName, final String fieldValue) {
        if (fieldValue.contains(",,")) {
            return Arrays.stream(fieldValue.split(",,"))
                    .map(value -> wrapFieldQuery(fieldName, value))
                    .collect(Collectors.joining(" OR "));
        } else {
            return wrapFieldQuery(fieldName, fieldValue);
        }
    }

    private String createQuery(Map<String, String> queryParts) {
        if (queryParts.containsKey("field") && queryParts.containsKey("value")) {
            final String field = CLIENT_SOLR_FIELD_MAPPING.containsKey(queryParts.get("field")) ?
                    CLIENT_SOLR_FIELD_MAPPING.get(queryParts.get("field")) : queryParts.get("field");
            final String value = queryParts.get("value");

            queryParts.remove("field");
            queryParts.remove("value");

            return String.format("_query_:\"{!edismax qf='%s' v='%s'}\"", field, value);
        }
        return "*:*";
    }

    private void renameOutFile(File outFile, String newFileName) {
        String pathToDir = outFile.getParent();
        boolean success = outFile.renameTo(new File(pathToDir + "/" + newFileName));
        if (!success)
            throw new IllegalStateException("Can't rename output file!");
    }
}
