package cz.mzk.fofola.configuration;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.api.KrameriusApi;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;


@Configuration
public class ApiConfiguration {

    @Bean
    public FedoraApi getFedoraApi(FofolaConfiguration config)
            throws ParserConfigurationException, TransformerConfigurationException {
        return new FedoraApi(config.getFedoraHost(), config.getFedoraUser(), config.getFedoraPswd());
    }

    @Bean
    public KrameriusApi getKrameriusApi(FofolaConfiguration config) {
        return new KrameriusApi(config.getKrameriusHost(), config.getKrameriusUser(), config.getKrameriusPswd());
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

    public static RestTemplate getConfiguredTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new PlusEncoderInterceptor()));
        return restTemplate;
    }

    private static class PlusEncoderInterceptor implements ClientHttpRequestInterceptor {
        // https://stackoverflow.com/questions/54294843/plus-sign-not-encoded-with-resttemplate-using-string-url-but-interpreted
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            return execution.execute(new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    URI u = super.getURI();
                    String strictlyEscapedQuery = StringUtils.replace(u.getRawQuery(), "+", "%2B");
                    return UriComponentsBuilder.fromUri(u)
                            .replaceQuery(strictlyEscapedQuery)
                            .build(true).toUri();
                }
            }, body);
        }
    }
}
