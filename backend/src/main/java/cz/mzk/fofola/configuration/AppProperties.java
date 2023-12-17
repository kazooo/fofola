package cz.mzk.fofola.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@Getter
public class AppProperties {

    /* General */
    @Value("${TIMEZONE:#{T(java.util.TimeZone).getDefault()}}")
    private TimeZone timezone;
    @Value("${REQUEST_CONNECTION_TIMEOUT:100000}")
    private Integer requestConnectionTimeout;
    @Value("${MAX_CONNECTIONS:10}")
    private Integer maxConnections;
    @Value("${MAX_CONNECTIONS_PER_ROUTE:10}")
    private Integer maxConnectionsPerRoute;

    /* Solr */
    @Value("${SOLR_HOST:http://localhost:8983/solr}")
    private String solrHost;
    @Value("${SOLR_SEARCH_CORE:search}")
    private String solrSearchCore;
    @Value("${SOLR_PROCESSING_CORE:processing}")
    private String solrProcessingCore;
    @Value("${SOLR_DOC_MAX_FETCH:1000}")
    private Integer solrMaxDocToFetch;
    @Value("${SOLR_OAUTH_PROXY_TOKEN:}")
    private String solrOauthProxyToken;

    /* Sugo */
    @Value("${SUGO_HOST:http://localhost:8083}")
    private String sugoHost;

    /* Kramerius */
    @Value("${KRAMERIUS_HOST:http://localhost}")
    private String krameriusHost;
    @Value("${KRAMERIUS_USER:krameriusUser}")
    private String krameriusUser;
    @Value("${KRAMERIUS_PSWD:krameriusPswd}")
    private String krameriusPswd;

    /* oAuth 2.0*/
    @Value("${OAUTH_TOKEN_URL:https://awesome-url/realms/kramerius/protocol/openid-connect/token}")
    private String oAuthTokenUrl;
    @Value("${OAUTH_CLIENT_ID:krameriusClient}")
    private String oAuthClientId;
    @Value("${OAUTH_CLIENT_SECRET:awesome-secret}")
    private String oAuthClientSecret;

    /* Postgres */
    @Value("${POSTGRES_DB_JDBC_URL:}")
    private String postgresJdbcUrl;
    @Value("${POSTGRES_DB_USER:}")
    private String postgresUser;
    @Value("${POSTGRES_DB_PSWD:}")
    private String postgresPswd;

    /* H2 */
    @Value("${H2_DB_JDBC_URL:jdbc:h2:mem:fofola}")
    private String h2JdbcUrl;
    @Value("${H2_DB_USER:user}")
    private String h2User;
    @Value("${H2_DB_PSWD:pswd}")
    private String h2Pswd;
}
