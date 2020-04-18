package cz.mzk.fofola.processes.vc_linker;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;


/**
 * The core class of linker. It collects all children of root uuid (by a cursor or a single request)
 * and then creates links to virtual collection in Fedora and update Solr records for all of them.
 *
 * @author Aleksei Ermak
 */
public class KrameriusVCLinker {

    // Solr client to collect children uuids
    private final HttpSolrClient solrClient;

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
        this.solrClient = new HttpSolrClient.Builder(solrHost)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();

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

        long numFound = queryForNumFound(query);

        if (numFound != 0) {
            if (numFound > maxDocsPerQuery) {
                query.setRows(maxDocsPerQuery);
                fetchByCursorAndLink(query, vcId);
            } else {
                query.setRows(Math.toIntExact(numFound));
                fetchByRequestAndLink(query, vcId);
            }
        }
    }

    private void fetchByCursorAndLink(SolrQuery query, String vcId)
            throws IOException, SolrServerException {
        query.setSort(SolrQuery.SortClause.asc("PID"));
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse rsp = querySolrClient(query);

            SolrDocumentList solrDocs = rsp.getResults();
            linkUuidsFromSolrDocs(solrDocs, vcId);

            String nextCursorMark = rsp.getNextCursorMark();
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
    }

    private void fetchByRequestAndLink(SolrQuery query, String vcId)
            throws IOException, SolrServerException {
        SolrDocumentList solrDocs = queryForSolrDocList(query);
        linkUuidsFromSolrDocs(solrDocs, vcId);
    }

    private void linkUuidsFromSolrDocs(SolrDocumentList solrDocs, String vcId) {
        for (SolrDocument solrDoc : solrDocs) {
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
        }
        try {
            solrVCLinker.commitChanges();
        } catch (IOException | SolrServerException e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    private long queryForNumFound(SolrQuery query) throws IOException, SolrServerException {
        query.setRows(0);
        SolrDocumentList docs = queryForSolrDocList(query);
        return docs.getNumFound();
    }

    private SolrDocumentList queryForSolrDocList(SolrQuery query) throws IOException, SolrServerException {
        QueryResponse response = querySolrClient(query);
        return response.getResults();
    }

    private QueryResponse querySolrClient(SolrQuery query) throws IOException, SolrServerException {
        return solrClient.query(query);
    }

    public void close() throws IOException {
        solrVCLinker.close(); // automatically closes 'solrClient'
        fedoraVCLinker.close();
    }
}
