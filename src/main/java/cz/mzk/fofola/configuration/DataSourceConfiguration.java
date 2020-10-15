package cz.mzk.fofola.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
@Slf4j
public class DataSourceConfiguration {

    @Bean
    public DataSource getDataSource(@Value("${POSTGRES_DB_JDBC_URL:}") String dbJdbcUrl,
                                    @Value("${POSTGRES_DB_USER:}") String dbUser,
                                    @Value("${POSTGRES_DB_PSWD:}") String dbPswd) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        if (!dbJdbcUrl.isEmpty() && !dbUser.isEmpty() && !dbPswd.isEmpty()) {
            log.info("Existing PostgreSQL database found, configuring connection...");
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
            dataSourceBuilder.url(dbJdbcUrl);
            dataSourceBuilder.username(dbUser);
            dataSourceBuilder.password(dbPswd);
        } else {
            log.info("No external PostgreSQL database found, configuring in-memory database...");
            dataSourceBuilder.driverClassName("org.h2.Driver");
            dataSourceBuilder.url("jdbc:h2:mem:fofola");
            dataSourceBuilder.username("SA");
            dataSourceBuilder.password("");
        }
        return dataSourceBuilder.build();
    }
}
