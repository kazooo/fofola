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
    private final DnntLabelEnum dnntLabel;
    private final String field;

    public SolrResponseWritingProcess(ProcessParams params) throws IOException {
        super(params);

        final FofolaConfiguration fofolaConfig = params.getConfig();
        final Map<String, ?> data = params.getData();

        model = (String) data.getOrDefault("model", null);
        accessibility = (String) data.getOrDefault("access", null);
        yearFrom = (String) data.get("yearFrom");
        yearTo = (String) data.get("yearTo");
        dnntLabel = DnntLabelEnum.of((String) data.getOrDefault("dnntLabel", null));
        field = (String) data.getOrDefault("field", null);

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
            queryParts.add(SolrField.MODEL_FIELD_NAME + ":\"" + model +"\"");
        }

        if (accessibility != null) {
            queryParts.add(SolrField.ACCESSIBILITY + ":\"" + accessibility +"\"");
        }

        if (yearTo != null || yearFrom != null) {
            queryParts.add(getYearRange());
        }

        if (dnntLabel != null) {
            queryParts.add(SolrField.DNNT_LABELS + ":\"" + dnntLabel.value() + "\"");
        }

        final String query = String.join(" AND ", queryParts);
        logger.info(query);

        final SolrQuery queryParams = new SolrQuery(query);
        queryParams.addField(field);

        return queryParams;
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
