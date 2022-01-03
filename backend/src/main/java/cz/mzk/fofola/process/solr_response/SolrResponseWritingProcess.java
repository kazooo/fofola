package cz.mzk.fofola.process.solr_response;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.enums.dnnt.DnntLabelEnum;
import cz.mzk.fofola.model.doc.SolrField;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SolrResponseWritingProcess extends Process {

    private final String solrHost;
    private final String model;
    private final String accessibility;
    private final String yearFrom;
    private final String yearTo;
    private final String dnntLabel;
    private final String field;

    public static final String ANY_FIELD_VALUE = "any";
    public static final String NO_FIELD_VALUE = "none";

    public SolrResponseWritingProcess(ProcessParams params) throws IOException {
        super(params);

        final FofolaConfiguration fofolaConfig = params.getConfig();
        final Map<String, Object> data = params.getData();

        yearFrom = (String) data.get("yearFrom");
        yearTo = (String) data.get("yearTo");
        model = (String) data.getOrDefault("model", ANY_FIELD_VALUE);
        accessibility = (String) data.getOrDefault("access", ANY_FIELD_VALUE);
        field = (String) data.getOrDefault("field", null);

        final String dnntLabelRaw = (String) data.getOrDefault("dnntLabel", ANY_FIELD_VALUE);
        final DnntLabelEnum dnntLabelEnum = DnntLabelEnum.of(dnntLabelRaw);
        if (dnntLabelEnum != null) {
            dnntLabel = dnntLabelEnum.value();
        } else {
            dnntLabel = dnntLabelRaw;
        }

        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public TerminationReason process() throws Exception {
        final SolrClient solrClient = SolrService.buildClient(solrHost);
        final String readyOutFileName = FileService.fileNameWithDateStampPrefix("solr-response.txt");
        final File notReadyOutFile = FileService.getSolrRespOutputFile("not-ready-" + readyOutFileName);
        final PrintWriter output = new PrintWriter(notReadyOutFile);
        final SolrQuery params = createQuery();

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

    private SolrQuery createQuery() {
        final List<String> queryParts = new ArrayList<>();

        if (model != null) {
            queryParts.add(wrapFieldQuery(SolrField.MODEL_FIELD_NAME, model));
        }

        if (accessibility != null) {
            queryParts.add(wrapFieldQuery(SolrField.ACCESSIBILITY, accessibility));

        }

        if (dnntLabel != null) {
            queryParts.add(wrapFieldQuery(SolrField.DNNT_LABELS, dnntLabel));
        }

        if (yearTo != null || yearFrom != null) {
            queryParts.add(getYearRange());
        }

        final String query = String.join(" AND ", queryParts);
        logger.info(query);

        final SolrQuery queryParams = new SolrQuery(query);
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

    private String getYearRange() {
        String range = SolrField.YEAR_FIELD_NAME + ":[";
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

    private void renameOutFile(File outFile, String newFileName) {
        String pathToDir = outFile.getParent();
        boolean success = outFile.renameTo(new File(pathToDir + "/" + newFileName));
        if (!success)
            throw new IllegalStateException("Can't rename output file!");
    }
}
