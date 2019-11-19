package cz.mzk.fofola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.logging.Logger;


@SpringBootApplication//(scanBasePackages={"cz.mzk.integrity"})
@EnableAsync
public class Application {

    private static final Logger LOG
            = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}