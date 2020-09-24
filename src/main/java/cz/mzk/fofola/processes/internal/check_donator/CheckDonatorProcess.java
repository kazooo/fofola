package cz.mzk.fofola.processes.internal.check_donator;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.models.Process;
import cz.mzk.fofola.processes.utils.FedoraClient;
import cz.mzk.fofola.processes.utils.FileUtils;
import cz.mzk.fofola.processes.utils.SolrUtils;
import cz.mzk.fofola.processes.utils.XMLUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.Consumer;


public class CheckDonatorProcess extends Process {

    private final String vcId;
    private final String donator;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;
    private final String solrHost;

    public CheckDonatorProcess(LinkedHashMap<String, Object> params,
                               FofolaConfiguration fofolaConfig) throws IOException {
        super(params);
        donator = (String) params.get("donator");
        vcId = (String) params.get("vcId");
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public void process() throws Exception {
        SolrClient solrClient = SolrUtils.buildClient(solrHost);
        FedoraClient fedoraClient = new FedoraClient(fedoraHost, fedoraUser, fedoraPswd);

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
                Document relsExt = fedoraClient.getRelsExt(uuid);
                Node descriptionRootNode = XMLUtils.getDescRootNode(relsExt);
                if (XMLUtils.getHasDonatorNode(donator, descriptionRootNode) == null) {
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
    }

    private void renameOutFile(File outFile, String newFileName) {
        String pathToDir = outFile.getParent();
        boolean success = outFile.renameTo(new File(pathToDir + "/" + newFileName));
        if (!success) throw new IllegalStateException("Can't rename output file!");
    }
}
