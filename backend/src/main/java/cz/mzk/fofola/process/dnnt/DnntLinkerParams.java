package cz.mzk.fofola.process.dnnt;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.enums.dnnt.DnntLabelEnum;
import cz.mzk.fofola.service.SolrService;
import lombok.Data;
import org.apache.solr.client.solrj.SolrClient;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Data
public class DnntLinkerParams {

    private final String label;
    private final SolrClient solrClient;
    private final FedoraApi fedoraApi;
    private final int maxDocsPerQuery;
    private final Logger logger;
    private final boolean processRecursive;

    public DnntLinkerParams(final Map<String, ?> data,
                            final FofolaConfiguration fofolaConfig,
                            final Logger logger,
                            final int maxDocsPerQuery) throws TransformerConfigurationException, ParserConfigurationException {
        label = Objects.requireNonNull(DnntLabelEnum.of((String) data.get("label"))).value();
        processRecursive = (Boolean) data.get("processRecursive");

        final String fedoraHost = fofolaConfig.getFedoraHost();
        final String fedoraUser = fofolaConfig.getFedoraUser();
        final String fedoraPswd = fofolaConfig.getFedoraPswd();
        final String solrHost = fofolaConfig.getSolrHost();

        this.solrClient = SolrService.buildClient(solrHost);
        this.fedoraApi = new FedoraApi(fedoraHost, fedoraUser, fedoraPswd);

        this.logger = logger;
        this.maxDocsPerQuery = maxDocsPerQuery;
    }
}
