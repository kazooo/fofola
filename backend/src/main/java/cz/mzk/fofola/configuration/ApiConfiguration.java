package cz.mzk.fofola.configuration;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.api.PlusEncoderInterceptor;
import cz.mzk.fofola.api.RestTemplateErrorHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.util.Collections;
import java.util.Map;


@Configuration
public class ApiConfiguration {

    @Bean
    public static FedoraApi getFedoraApi(final FofolaConfiguration config)
            throws ParserConfigurationException, TransformerConfigurationException {
        final ClientHttpRequestFactory factory = createRequestFactory(config);
        return new FedoraApi(
                config.getFedoraHost(),
                config.getFedoraUser(),
                config.getFedoraPswd(),
                createConfiguredTemplate(factory)
        );
    }

    @Bean
    public static KrameriusApi getKrameriusApi(final FofolaConfiguration config) {
        final ClientHttpRequestFactory factory = createRequestFactory(config);
        return new KrameriusApi(
                config.getKrameriusHost(),
                config.getKrameriusUser(),
                config.getKrameriusPswd(),
                createConfiguredTemplate(factory)
        );
    }

    @Bean
    public static ClientHttpRequestFactory createRequestFactory(final FofolaConfiguration config) {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());

        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getRequestConnectionTimeout())
                .build();

        final CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    public static HttpHeaders createAuthHeaders(String user, String pswd) {
        final String credentials = user + ":" + pswd;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.add("User-Agent", "Fofola");
        return httpHeaders;
    }

    public static String buildUri(String url, Map<String, ?> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.encode().build().toUri().toString();
    }

    private static RestTemplate createConfiguredTemplate(final ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        restTemplate.setInterceptors(Collections.singletonList(new PlusEncoderInterceptor()));
        return restTemplate;
    }
}
