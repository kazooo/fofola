package cz.mzk.fofola.processes.vc_linker;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * The main purpose of object of this class is updating Solr records.
 * It updates 'collection' and 'modified_date' by adding virtual collection ID
 * and setting the timestamp of the last changes.
 *
 * @author Aleksei Ermak
 */
public class SolrVCLinker {

    private final HttpSolrClient solrClient;
    private final SimpleDateFormat dateFormat;

    public SolrVCLinker(HttpSolrClient solrClient) {
        this.solrClient = solrClient;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public void indexVCFor(String docPID, String vcId) throws IOException, SolrServerException {
        SolrInputDocument inputDoc = new SolrInputDocument();
        inputDoc.addField("PID",docPID);

        // add virtual collection id
        Map<String,Object> collectionFieldModifier = new HashMap<>(1);
        collectionFieldModifier.put("add", vcId);
        inputDoc.addField("collection", collectionFieldModifier);

        // update last modified date field
        Map<String, Object> modifiedDateFieldModifier = new HashMap<>(1);
        modifiedDateFieldModifier.put("set", dateFormat.format(new Date()));
        inputDoc.addField("modified_date", modifiedDateFieldModifier);

        solrClient.add(inputDoc);
    }

    public void commitChanges() throws IOException, SolrServerException {
        solrClient.commit();
    }

    public void close() throws IOException {
        solrClient.close();
    }
}
