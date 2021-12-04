package cz.mzk.fofola.configuration;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization(final FofolaConfiguration config) {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(config.getTimezone());
    }
}
