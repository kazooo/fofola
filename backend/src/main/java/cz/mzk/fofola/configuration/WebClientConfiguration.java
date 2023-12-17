package cz.mzk.fofola.configuration;

import cz.mzk.fofola.api.utils.PlusEncoderInterceptor;
import cz.mzk.fofola.api.utils.RestTemplateErrorHandler;
import lombok.AllArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
@AllArgsConstructor
public class WebClientConfiguration {

    @Bean
    @Qualifier("kramerius")
    public RestTemplate krameriusRestTemplate(final AppProperties props) {
        final RestTemplate template = createKrameriusOAuthRestTemplate(props);
        setup(template);
        return template;
    }

    @Bean
    @Qualifier("sugo")
    public RestTemplate sugoRestTemplate(final AppProperties props) {
        final ClientHttpRequestFactory factory = createRequestFactory(props);
        RestTemplate template = new RestTemplate(factory);
        setup(template);
        return template;
    }

    private RestTemplate createKrameriusOAuthRestTemplate(final AppProperties props) {
        final AccessTokenRequest atr = new DefaultAccessTokenRequest();
        final OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(oauth2Resource(props), new DefaultOAuth2ClientContext(atr));
        oauth2RestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return oauth2RestTemplate;
    }

    @Bean
    protected OAuth2ProtectedResourceDetails oauth2Resource(final AppProperties props) {
        final ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setUsername(props.getKrameriusUser());
        details.setPassword(props.getKrameriusPswd());
        details.setAccessTokenUri(props.getOAuthTokenUrl());
        details.setClientId(props.getOAuthClientId());
        details.setClientSecret(props.getOAuthClientSecret());
        details.setTokenName(OAuth2AccessToken.ACCESS_TOKEN);
        return details;
    }

    @Bean
    public static ClientHttpRequestFactory createRequestFactory(final AppProperties props) {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(props.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(props.getMaxConnectionsPerRoute());

        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(props.getRequestConnectionTimeout())
                .build();

        final CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    private void setup(final RestTemplate template) {
        template.setErrorHandler(new RestTemplateErrorHandler());
        template.setInterceptors(Collections.singletonList(new PlusEncoderInterceptor()));
        template.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public static HttpHeaders createCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Fofola");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
