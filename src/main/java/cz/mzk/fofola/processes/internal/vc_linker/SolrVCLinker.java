package cz.mzk.fofola.processes.internal.vc_linker;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * The main purpose of object of this class is updating Solr records.
 * It updates 'collection' and 'modified_date' by adding virtual collection ID
 * and setting the timestamp of the last changes.
 *
 * @author Aleksei Ermak
 */
public class SolrVCLinker {

    private final SolrClient solrClient;
    private final SimpleDateFormat dateFormat;

    public SolrVCLinker(SolrClient solrClient) {
        this.solrClient = solrClient;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public void indexVCFor(String uuid, String vcId)
            throws IOException, SolrServerException {
        modifyAndSend(uuid, vcId, "add");
    }

    public void setVCFor(String uuid, List<String> collections)
            throws IOException, SolrServerException {
        // unfortunately 'remove' operation doesn't work for partial update in SolrJ :(
        modifyAndSend(uuid, collections, "set");
    }

    public void modifyAndSend(String uuid, Object collection, String modifier)
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
