package cz.mzk.fofola.configuration;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.api.KrameriusApi;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.xml.parsers.ParserConfigurationException;


@Configuration
public class ApiConfiguration {

    @Bean
    public FedoraApi getFedoraApi(FofolaConfiguration config) throws ParserConfigurationException {
        return new FedoraApi(config.getFedoraHost(), config.getFedoraUser(), config.getFedoraPswd());
    }

    @Bean
    public KrameriusApi getKrameriusApi(FofolaConfiguration config) {
        return new KrameriusApi(config.getKrameriusHost(), config.getKrameriusUser(), config.getKrameriusPswd());
    }

    public static HttpHeaders createAuthHeaders(String user, String pswd) {
        final String credentials = user + ":" + pswd;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.add("User-Agent", "Fofola");
        return httpHeaders;
    }
}
