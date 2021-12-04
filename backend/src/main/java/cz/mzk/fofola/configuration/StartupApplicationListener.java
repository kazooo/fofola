package cz.mzk.fofola.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
@AllArgsConstructor
@Slf4j
public class StartupApplicationListener {

    private final FofolaConfiguration config;

    @PostConstruct
    public void setApplicationTimeZone() {
        log.info("Setup a timezone for the application: " + config.getTimezone().getDisplayName());
        TimeZone.setDefault(config.getTimezone());
    }
}
