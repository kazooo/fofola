package cz.mzk.fofola.process.vc_linker;

import cz.mzk.fofola.service.SolrService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;


/**
 * The main purpose of object of this class is updating Solr records.
 * It updates 'collection' and 'modified_date' by adding virtual collection ID
 * and setting the timestamp of the last changes.
 *
 * @author Aleksei Ermak
 */

public class SolrVCLinker {

    private final Logger logger;
    private final SolrClient solrClient;
    private final SimpleDateFormat dateFormat;

    public SolrVCLinker(SolrClient solrClient, Logger log) {
        logger = log;
        this.solrClient = solrClient;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public void addVc(String vcId, String uuid) throws IOException, SolrServerException {
        SolrQuery params = createSolrQuery(uuid);
        SolrService.paginateByCursor(
                params, solrClient, createLinkConsumer(vcId), 1500
        );
    }

    @SuppressWarnings("unchecked")
    private Consumer<SolrDocument> createLinkConsumer(String vcId) {
        return solrDoc -> {
            List<String> collections = (List<String>) solrDoc.getFieldValue("collection");
            if (collections == null || !collections.contains(vcId)) {
                try {
                    String uuid = (String) solrDoc.getFieldValue("PID");
                    logger.info("Update: " + uuid);
                    addVCFor(uuid, vcId);
                } catch (Exception e) {
                    logger.warning("\n\n" + e.getMessage());
                    logger.warning(Arrays.toString(e.getStackTrace()) + "\n\n");
                }
            }
        };
    }

    public void removeVc(String vcId, String uuid) throws IOException, SolrServerException {
        SolrQuery params = createSolrQuery(uuid);
        SolrService.paginateByCursor(
                params, solrClient, createUnlinkConsumer(vcId), 1500
        );
    }

    @SuppressWarnings("unchecked")
    private Consumer<SolrDocument> createUnlinkConsumer(String vcId) {
        return solrDoc -> {
            List<String> collections = (List<String>) solrDoc.getFieldValue("collection");
            if (collections != null && collections.contains(vcId)) {
                collections.remove(vcId);
                try {
                    String uuid = (String) solrDoc.getFieldValue("PID");
                    logger.info("Update: " + uuid);
                    setVCFor(uuid, collections);
                } catch (Exception e) {
                    logger.warning("\n\n" + e.getMessage());
                    logger.warning(Arrays.toString(e.getStackTrace()) + "\n\n");
                }
            }
        };
    }

    private SolrQuery createSolrQuery(String uuid) {
        String allPartsQueryStr = "root_pid:\"" + uuid.trim() + "\"";
        SolrQuery query = new SolrQuery(allPartsQueryStr);
        query.addField("PID");
        query.addField("collection");
        return query;
    }

    private void addVCFor(String uuid, String vcId)
            throws IOException, SolrServerException {
        modifyAndSend(uuid, vcId, "add");
    }

    private void setVCFor(String uuid, List<String> collections)
            throws IOException, SolrServerException {
        // unfortunately 'remove' operation doesn't work for partial update in SolrJ :(
        modifyAndSend(uuid, collections, "set");
    }

    private void modifyAndSend(String uuid, Object collection, String modifier)
            throws IOException, SolrServerException {
        SolrInputDocument inputDoc = new SolrInputDocument();
        inputDoc.addField("PID", uuid);
        inputDoc.addField("collection", Collections.singletonMap(modifier, collection));

        // update last modified date field
        inputDoc.addField("modified_date",
                Collections.singletonMap("set", dateFormat.format(new Date())));
        solrClient.add(inputDoc);
    }

    public void commitChanges() throws IOException, SolrServerException {
        solrClient.commit();
    }

    public void close() throws IOException {
        solrClient.close();
    }
}
