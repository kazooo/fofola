package cz.mzk.fofola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class Application {

    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }
}
