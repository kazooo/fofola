package cz.mzk.fofola.process.solr_response;

import cz.mzk.fofola.configuration.FofolaConfiguration;
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

    public SolrResponseWritingProcess(ProcessParams params) throws IOException {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();
        model = (String) data.get("model");
        accessibility = (String) data.get("accessibility");
        yearFrom = (String) data.get("year_from");
        yearTo = (String) data.get("year_to");
        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public TerminationReason process() throws Exception {
        SolrClient solrClient = SolrService.buildClient(solrHost);
        String readyOutFileName = FileService.fileNameWithDateStampPrefix("solr-response.txt");
        File notReadyOutFile = FileService.getSolrRespOutputFile("not-ready-" + readyOutFileName);
        PrintWriter output = new PrintWriter(notReadyOutFile);
        SolrQuery params = createQuery();

        Consumer<SolrDocument> logic = solrDoc -> {
            String uuid = (String) solrDoc.getFieldValue(SolrField.UUID_FIELD_NAME);
            output.println(uuid);
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
        List<String> queryParts = new ArrayList<>();
        if (model != null)
            queryParts.add(SolrField.MODEL_FIELD_NAME + ":\"" + model +"\"");
        if (accessibility != null)
            queryParts.add(SolrField.ACCESSIBILITY + ":\"" + accessibility +"\"");
        if (yearTo != null || yearFrom != null)
            queryParts.add(getYearRange());
        String query = String.join(" AND ", queryParts);
        logger.info(query);
        SolrQuery queryParams = new SolrQuery(query);
        queryParams.addField(SolrField.UUID_FIELD_NAME);
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
