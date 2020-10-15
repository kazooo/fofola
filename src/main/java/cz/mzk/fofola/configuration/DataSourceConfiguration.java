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
    public DataSource getDataSource(FofolaConfiguration config) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        if (!config.getDbJdbcUrl().isEmpty() &&
            !config.getDbUser().isEmpty() &&
            !config.getDbPswd().isEmpty()) {
            log.info("Existing PostgreSQL database found, configuring connection...");
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
            dataSourceBuilder.url(config.getDbJdbcUrl());
            dataSourceBuilder.username(config.getDbUser());
            dataSourceBuilder.password(config.getDbPswd());
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
