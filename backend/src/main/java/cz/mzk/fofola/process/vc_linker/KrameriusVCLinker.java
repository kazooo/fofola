package cz.mzk.fofola.process.vc_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.service.SolrService;
import cz.mzk.fofola.service.UuidService;
import org.apache.solr.client.solrj.SolrServerException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * The core class of linker. It collects all children of root uuid (by a cursor or a single request)
 * and then creates links to virtual collection in Fedora and update Solr records for all of them.
 *
 * @author Aleksei Ermak
 */
public class KrameriusVCLinker {

    private final SolrVCLinker solrVCLinker;
    private final FedoraVCLinker fedoraVCLinker;

    public KrameriusVCLinker(final FofolaConfiguration configuration, Logger logger)
            throws ParserConfigurationException, TransformerConfigurationException, XPathExpressionException {
        solrVCLinker = new SolrVCLinker(SolrService.buildClient(configuration.getSolrHost()), logger);
        fedoraVCLinker = new FedoraVCLinker(configuration, logger);
    }

    public void unlinkFromVcByRootUuid(String vcId, String rootUuid)
            throws XPathExpressionException, TransformerException, IOException, SolrServerException {
        rootUuid = UuidService.makeUuid(rootUuid);
        fedoraVCLinker.removeVcRecursively(vcId, rootUuid);
        solrVCLinker.removeVc(vcId, rootUuid);
    }

    public void linkToVcByRootUuid(String vcId, String rootUuid)
            throws TransformerException, XPathExpressionException, IOException, SolrServerException {
        rootUuid = UuidService.makeUuid(rootUuid);
        fedoraVCLinker.addVcRecursively(vcId, rootUuid);
        solrVCLinker.addVc(vcId, rootUuid);
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrVCLinker.commitChanges();
        solrVCLinker.close(); // automatically closes 'solrClient'
    }
}
