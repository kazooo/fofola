package cz.mzk.fofola.configuration;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.model.solr.ProcessingDoc;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.SolrRepository;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class AdapterConfiguration {

    @Bean
    public static KrameriusApi getKrameriusApi(final AppProperties props, @Qualifier("kramerius") final RestTemplate template) {
        return new KrameriusApi(props, template);
    }

    @Bean
    public static SugoApi getSugoApi(final AppProperties props, @Qualifier("sugo") final RestTemplate template) {
        return new SugoApi(props.getSugoHost(), template);
    }

    @Bean
    public SolrRepository<SearchDoc> solrSearchCoreRepository(final AppProperties props) {
        return new SolrRepository<>(buildSolrClient(props.getSolrSearchCore(), props), props, SearchDoc.class);
    }

    @Bean
    public SolrRepository<ProcessingDoc> solrProcessingCoreRepository(final AppProperties props) {
        return new SolrRepository<>(buildSolrClient(props.getSolrProcessingCore(), props), props, ProcessingDoc.class);
    }

    private static SolrClient buildSolrClient(final String core, final AppProperties props) {
        final HttpRequestInterceptor cookieInterceptor = (request, context) ->
                request.addHeader("Cookie", "k7_oauth2_proxy=" + props.getSolrOauthProxyToken());

        final CloseableHttpClient httpClient = HttpClients.custom()
                .setMaxConnPerRoute(props.getMaxConnectionsPerRoute())
                .setMaxConnTotal(props.getMaxConnections())
                .addInterceptorFirst(cookieInterceptor)
                .build();

        return new HttpSolrClient.Builder(props.getSolrHost() + "/" + core)
                .withHttpClient(httpClient)
                .build();
    }
}
