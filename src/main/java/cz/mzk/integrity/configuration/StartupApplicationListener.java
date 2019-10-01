package cz.mzk.integrity.configuration;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StartupApplicationListener {

    public static String appStartupTime;

    @PostConstruct
    private void init() {
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
        Date date = new Date();
        appStartupTime = dt.format(date);
    }
}