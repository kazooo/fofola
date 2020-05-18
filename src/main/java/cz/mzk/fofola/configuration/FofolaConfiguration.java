package cz.mzk.fofola.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FofolaConfiguration {

    @Value("${FEDORA_HOST}")
    private String fedoraHost;
    @Value("${FEDORA_USER}")
    private String fedoraUser;
    @Value("${FEDORA_PSWD}")
    private String fedoraPswd;
    @Value("${SOLR_HOST}")
    private String solrHost;

    public String getFedoraHost() {
        return fedoraHost;
    }

    public String getFedoraPswd() {
        return fedoraPswd;
    }

    public String getFedoraUser() {
        return fedoraUser;
    }

    public String getSolrHost() {
        return solrHost;
    }
}
