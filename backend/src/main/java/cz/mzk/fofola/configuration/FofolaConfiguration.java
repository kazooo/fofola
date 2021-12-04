package cz.mzk.fofola.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@Getter
public class FofolaConfiguration {

    /* Fedora */
    @Value("${FEDORA_HOST:http://localhost:8080/fedora}")
    private String fedoraHost;
    @Value("${FEDORA_USER:fedoraUser}")
    private String fedoraUser;
    @Value("${FEDORA_PSWD:fedoraPswd}")
    private String fedoraPswd;

    /* Solr */
    @Value("${SOLR_HOST:http://localhost:8983/solr/kramerius}")
    private String solrHost;

    /* Kramerius */
    @Value("${KRAMERIUS_HOST:http://localhost}")
    private String krameriusHost;
    @Value("${KRAMERIUS_USER:krameriusUser}")
    private String krameriusUser;
    @Value("${KRAMERIUS_PSWD:krameriusPswd}")
    private String krameriusPswd;

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

    /* Additional */
    @Value("${TIMEZONE:#{T(java.util.TimeZone).getDefault()}}")
    private TimeZone timezone;
}
