package cz.mzk.fofola.processes.internal.vc_linker;

import cz.mzk.fofola.processes.utils.SolrUtils;
import cz.mzk.fofola.processes.utils.UuidUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Logger;


/**
 * The core class of linker. It collects all children of root uuid (by a cursor or a single request)
 * and then creates links to virtual collection in Fedora and update Solr records for all of them.
 *
 * @author Aleksei Ermak
 */
public class KrameriusVCLinker {

    // Solr client to collect children uuids
    private final SolrClient solrClient;

    private final SolrVCLinker solrVCLinker;
    private final FedoraVCLinker fedoraVCLinker;

    // depends on this parameter can choose
    // whether use cursor or a single request
    // to collect children uuids
    private final int maxDocsPerQuery;

    private final Logger logger;

    public KrameriusVCLinker(String fedoraHost, String fedoraUser, String fedoraPswd,
                             String solrHost, int maxDocsPerQuery, Logger logger)
            throws ParserConfigurationException, TransformerConfigurationException {
        this.solrClient = SolrUtils.buildClient(solrHost);
        this.solrVCLinker = new SolrVCLinker(solrClient);
        this.fedoraVCLinker = new FedoraVCLinker(fedoraHost, fedoraUser, fedoraPswd);
        this.maxDocsPerQuery = maxDocsPerQuery;
        this.logger = logger;
    }

    public void linkRootAndChildrenToVc(String vcId, String rootUuid)
            throws IOException, SolrServerException {
        vcId = UuidUtils.checkAndMakeVcId(vcId);
        rootUuid = UuidUtils.checkAndMakeUuid(rootUuid);
        String allPartsQueryStr = "pid_path:/.*" + rootUuid.trim() + ".*/";
        SolrQuery query = new SolrQuery(allPartsQueryStr);
        query.addField("PID");

        Consumer<SolrDocument> publisherLogic = generateConsumer(vcId);
        SolrUtils.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, publisherLogic, maxDocsPerQuery
        );
    }

    private Consumer<SolrDocument> generateConsumer(String vcId) {
        return solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue("PID");
            logger.info("Try " + docPID + "...");
            boolean tripletCreated = createFedoraLink(docPID, vcId);
            if (tripletCreated) {
                createSolrLink(docPID, vcId);
            } else {
                logger.info(docPID + " : collection link already exists or exception occurs");
            }
        };
    }

    private boolean createFedoraLink(String uuid, String vcId) {
        try {
            return fedoraVCLinker.writeVCFor(uuid, vcId);
        } catch (Exception e) {
            logger.warning("\n\n" + e.getMessage());
            logger.warning(Arrays.toString(e.getStackTrace()) + "\n\n");
            return false;
        }
    }

    private void createSolrLink(String uuid, String vcId) {
        try {
            solrVCLinker.indexVCFor(uuid, vcId);
        } catch (Exception e) {
            logger.warning("\n\n" + e.getMessage());
            logger.warning(Arrays.toString(e.getStackTrace()) + "\n\n");
        }
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrVCLinker.commitChanges();
        solrVCLinker.close(); // automatically closes 'solrClient'
    }
}
