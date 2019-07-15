package cz.mzk.integrity.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.util.logging.Logger;

@Configuration
@ComponentScan
@EnableSolrRepositories(
        basePackages = "cz.mzk.integrity.repository"
)
public class SolrConfiguration {

    @Value("${spring.data.solr.host}") String solrURL;

    private static final Logger logger = Logger.getLogger(SolrConfiguration.class.getName());

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder(solrURL).build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }
}
