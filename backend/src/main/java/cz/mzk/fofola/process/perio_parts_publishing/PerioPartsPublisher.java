package cz.mzk.fofola.process.perio_parts_publishing;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.service.SolrService;
import cz.mzk.fofola.service.UuidService;
import cz.mzk.fofola.repository.FedoraDocumentRepository;
import cz.mzk.fofola.service.XMLService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;


public class PerioPartsPublisher {

    private final FedoraDocumentRepository fedoraRepository;
    private final SolrClient solrClient;
    private final Logger logger;
    private final int maxDocs;

    public PerioPartsPublisher(String fedoraHost, String fedoraUser, String fedoraPswd,
                               String solrHost, int maxDocsPerQuery, Logger logger)
            throws ParserConfigurationException, TransformerConfigurationException, XPathExpressionException {
        this.logger = logger;
        maxDocs = maxDocsPerQuery;
        solrClient = SolrService.buildClient(solrHost);
        fedoraRepository = new FedoraDocumentRepository(new XMLService(), new FedoraApi(fedoraHost, fedoraUser, fedoraPswd));
    }

    public void checkPartsAndMakePublic(String rootUuid) {
        rootUuid = UuidService.makeUuid(rootUuid);
        SolrQuery query = new SolrQuery("PID:\"" + rootUuid + "\"");
        query.setFields("PID");
        try {
            SolrDocument perioDoc = SolrService.queryFirstSolrDoc(query, solrClient);
            checkAndPublishDocsRecursively(perioDoc, "");
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAndPublishDocsRecursively(SolrDocument doc, String indent)
            throws IOException, SolrServerException{
        AtomicBoolean makePublic = new AtomicBoolean(false);
        String uuid = (String) doc.getFieldValue("PID");
        SolrQuery childQuery = createQueryForChildNodes(uuid);
        Consumer<SolrDocument> publisherConsumer = solrDoc -> {
            String accessibility = (String) solrDoc.getFieldValue("dostupnost");
            if (accessibility.equals("public")) {
                makePublic.set(true);
                logger.info(indent+"Public document detected, make its parent public too...");
            } else {
                try {
                    boolean childArePublic = checkAndPublishDocsRecursively(solrDoc, "    "+indent);
                    if (childArePublic) makePublic.set(true);
                } catch (IOException | SolrServerException ignored) { }
            }
        };
        SolrService.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                childQuery, solrClient, publisherConsumer, maxDocs
        );
        if (makePublic.get()) {
            logger.info(indent + uuid + " make public!");
            SolrService.makePublic(uuid, solrClient);
            fedoraRepository.makePublic(uuid);
        }
        return makePublic.get();
    }

    private SolrQuery createQueryForChildNodes(String parentUuid) {
        String queryStr =
                "parent_pid:\"" + parentUuid + "\"" +
                " AND !PID:\"" + parentUuid + "\"" +
                " AND !fedora.model:\"page\"";
        SolrQuery query = new SolrQuery(queryStr);
        query.setFields("PID", "dostupnost");
        return query;
    }

    public void close() throws IOException {
        solrClient.close();
    }
}
