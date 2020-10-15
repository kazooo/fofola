package cz.mzk.fofola.process.internal.check_donator;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import cz.mzk.fofola.process.utils.FileUtils;
import cz.mzk.fofola.process.utils.SolrUtils;
import cz.mzk.fofola.service.XMLService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.w3c.dom.Document;
import cz.mzk.fofola.model.process.Process;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;


public class CheckDonatorProcess extends Process {

    private final String vcId;
    private final String donator;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;
    private final String solrHost;

    public CheckDonatorProcess(ProcessParams params) throws IOException {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();

        donator = (String) data.get("donator");
        vcId = (String) data.get("vcId");
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public TerminationReason process() throws Exception {
        XMLService xmlService = new XMLService();
        SolrClient solrClient = SolrUtils.buildClient(solrHost);
        FedoraApi fedoraApi = new FedoraApi(fedoraHost, fedoraUser, fedoraPswd);

        String readyOutFileName = FileUtils.fileNameWithDateStampPrefix(donator + ".txt");
        File notReadyOutFile = FileUtils.getCheckDonatorOutFile("not-ready-" + readyOutFileName);
        PrintWriter output = new PrintWriter(notReadyOutFile);

        String query = SolrUtils.wrapQueryStr(SolrUtils.COLLECTION_FIELD_NAME, vcId) + " AND NOT " +
                SolrUtils.wrapQueryStr(SolrUtils.MODEL_FIELD_NAME, "page");
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addField(SolrUtils.UUID_FIELD_NAME);
        solrQuery.addField(SolrUtils.ROOT_PID_FIELD_NAME);

        Consumer<SolrDocument> checkDonatorLogic = solrDoc -> {
            String uuid = (String) solrDoc.getFieldValue(SolrUtils.UUID_FIELD_NAME);
            String rootUuid = (String) solrDoc.getFieldValue(SolrUtils.ROOT_PID_FIELD_NAME);
            if (!uuid.equals(rootUuid)) return; // check only roots

            logger.info(uuid);
            try {
                Document relsExt = fedoraApi.getRelsExt(uuid);
                if (xmlService.getHasDonatorNode(donator, relsExt) == null) {
                    output.println(uuid);
                }
            } catch (Exception e) {
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        };
        SolrUtils.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(solrQuery, solrClient, checkDonatorLogic, 1000);

        output.flush();
        output.close();
        renameOutFile(notReadyOutFile, readyOutFileName);
        return null;
    }

    private void renameOutFile(File outFile, String newFileName) {
        String pathToDir = outFile.getParent();
        boolean success = outFile.renameTo(new File(pathToDir + "/" + newFileName));
        if (!success) throw new IllegalStateException("Can't rename output file!");
    }
}
