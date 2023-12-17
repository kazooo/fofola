package cz.mzk.fofola.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
@Slf4j
public class DataSourceConfiguration {

    @Bean
    public DataSource getDataSource(final AppProperties config) {
        final DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        if (externalDbExists(config)) {
            log.info("Existing PostgreSQL database found, configuring connection...");
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
            dataSourceBuilder.url(config.getPostgresJdbcUrl());
            dataSourceBuilder.username(config.getPostgresUser());
            dataSourceBuilder.password(config.getPostgresPswd());
        } else {
            log.info("No external PostgreSQL database found, configuring in-memory database...");
            dataSourceBuilder.driverClassName("org.h2.Driver");
            dataSourceBuilder.url(config.getH2JdbcUrl());
            dataSourceBuilder.username(config.getH2User());
            dataSourceBuilder.password(config.getH2Pswd());
        }
        return dataSourceBuilder.build();
    }

    private boolean externalDbExists(final AppProperties config) {
        return !config.getPostgresJdbcUrl().isEmpty() &&
                !config.getPostgresUser().isEmpty() &&
                !config.getPostgresPswd().isEmpty();
    }
}
