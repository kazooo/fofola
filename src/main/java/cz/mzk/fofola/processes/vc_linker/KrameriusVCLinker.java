package cz.mzk.fofola.processes.vc_linker;

import cz.mzk.fofola.processes.utils.SolrUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
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
        String allPartsQueryStr = "root_pid:\"" + rootUuid.trim() + "\"";
        SolrQuery query = new SolrQuery(allPartsQueryStr);
        query.addField("PID");

        Consumer<SolrDocument> publisher = generateConsumer(vcId);
        SolrUtils.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, publisher, maxDocsPerQuery
        );
    }

    private Consumer<SolrDocument> generateConsumer(String vcId) {
        return solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue("PID");
            boolean tripletCreated = false;
            try {
                tripletCreated = fedoraVCLinker.writeVCFor(docPID, vcId);
            } catch (SAXException | TransformerException | IOException e) {
                logger.warning(Arrays.toString(e.getStackTrace()));
            }
            try {
                if (tripletCreated) {
                    logger.info(docPID + " : Fedora link created");
                    solrVCLinker.indexVCFor(docPID, vcId);
                    logger.info(docPID + " : 'collection' indexed");
                } else {
                    logger.info(docPID + " : collection link already exists or exception occurs");
                }
            } catch (IOException | SolrServerException e) {
                logger.warning(Arrays.toString(e.getStackTrace()));
            }
        };
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrVCLinker.commitChanges();
        solrVCLinker.close(); // automatically closes 'solrClient'
        fedoraVCLinker.close();
    }
}
