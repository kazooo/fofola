package cz.mzk.fofola.process.internal.vc_linker;

import cz.mzk.fofola.process.utils.SolrUtils;
import cz.mzk.fofola.process.utils.UuidUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
        Consumer<SolrDocument> linkingLogic = createLinkConsumer(vcId);
        handleVCForBook(rootUuid, linkingLogic);
    }

    public void unlinkRootAndChildrenFromVc(String vcId, String rootUuid)
            throws IOException, SolrServerException {
        Consumer<SolrDocument> unlinkingLogic = createUnlinkConsumer(vcId);
        handleVCForBook(rootUuid, unlinkingLogic);
    }

    private void handleVCForBook(String rootUuid, Consumer<SolrDocument> logic)
            throws IOException, SolrServerException {
        rootUuid = UuidUtils.checkAndMakeUuid(rootUuid);
        SolrQuery query = createSolrQuery(rootUuid);
        SolrUtils.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, logic, maxDocsPerQuery
        );
    }

    private Consumer<SolrDocument> createLinkConsumer(String vcId) {
        return solrDoc -> {
            String uuid = (String) solrDoc.getFieldValue("PID");
            logger.info("Try " + uuid + "...");
            boolean tripletCreated = createFedoraLink(uuid, vcId);
            if (tripletCreated) {
                createSolrLink(uuid, vcId);
            } else {
                logger.info(uuid + " : collection link already exists or exception occurs");
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Consumer<SolrDocument> createUnlinkConsumer(String vcId) {
        return solrDoc -> {
            String uuid = (String) solrDoc.getFieldValue("PID");
            logger.info("Try " + uuid + "...");
            removeFedoraLink(uuid, vcId);
            List<String> collections = (List<String>) solrDoc.getFieldValue("collection");
            removeSolrLink(uuid, vcId, collections);
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

    private boolean removeFedoraLink(String uuid, String vcId) {
        try {
            return fedoraVCLinker.removeVCFor(uuid, vcId);
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

    private void removeSolrLink(String uuid, String vcId, List<String> collections) {
        try {
            if (!collections.contains(vcId)) return;
            collections.remove(vcId);
            solrVCLinker.setVCFor(uuid, collections);
        } catch (Exception e) {
            logger.warning("\n\n" + e.getMessage());
            logger.warning(Arrays.toString(e.getStackTrace()) + "\n\n");
        }
    }

    private SolrQuery createSolrQuery(String rootUuid) {
        String allPartsQueryStr = "pid_path:/" + rootUuid.trim() + ".*/";
        SolrQuery query = new SolrQuery(allPartsQueryStr);
        query.addField("PID");
        query.addField("collection");
        return query;
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrVCLinker.commitChanges();
        solrVCLinker.close(); // automatically closes 'solrClient'
    }
}
