package cz.mzk.fofola.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FofolaConfiguration {

    @Value("${FEDORA_HOST:http://localhost:8080/fedora}")
    private String fedoraHost;
    @Value("${FEDORA_USER:fedoraUser}")
    private String fedoraUser;
    @Value("${FEDORA_PSWD:fedoraPswd}")
    private String fedoraPswd;
    @Value("${SOLR_HOST:http://localhost:8983/solr/kramerius}")
    private String solrHost;
    @Value("${KRAMERIUS_HOST:http://localhost}")
    private String krameriusHost;
    @Value("${KRAMERIUS_USER:krameriusUser}")
    private String krameriusUser;
    @Value("${KRAMERIUS_PSWD:krameriusPswd}")
    private String krameriusPswd;
}
