package cz.mzk.fofola.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.model.KrameriusProcess;
import cz.mzk.fofola.model.vc.VC;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class KrameriusApi {

    private final String krameriusHost;
    private final RestTemplate restTemplate;
    private final HttpHeaders authHeaders;
    private final HttpEntity<String> authHttpEntity;
    private static final Gson gson = new Gson();

    private static final String CLIENT_API_V4 = "/search/api/v4.6";
    private static final String CLIENT_API_V5 = "/search/api/v5.0";

    public KrameriusApi(String kh, String ku, String kp) {
        krameriusHost = kh;
        restTemplate = ApiConfiguration.getConfiguredTemplate();
        authHeaders = ApiConfiguration.createAuthHeaders(ku, kp);
        authHttpEntity = new HttpEntity<>(authHeaders);
    }

    public KrameriusProcess planNewProcess(String def, String... ps) {
        String url = krameriusHost + CLIENT_API_V4 + "/processes?def=" + def;
        HttpHeaders requestHeaders = new HttpHeaders(authHeaders);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Parameters params = new Parameters(ps);
        HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(params), requestHeaders);
        ResponseEntity<KrameriusProcess> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KrameriusProcess.class);
        return response.getBody();
    }

    public KrameriusProcess getProcess(String uuid) {
        String url = krameriusHost + CLIENT_API_V4 + "/processes/" + uuid;
        ResponseEntity<KrameriusProcess> response = restTemplate.exchange(url, HttpMethod.GET, authHttpEntity, KrameriusProcess.class);
        KrameriusProcess krameriusProcess = Objects.requireNonNull(response.getBody());
        krameriusProcess.generateLogUrl(krameriusHost);
        return krameriusProcess;
    }

    public List<KrameriusProcess> getProcesses(Map<String, String> params) {
        String url = krameriusHost + CLIENT_API_V4 + "/processes";
        url = ApiConfiguration.buildUri(url, params);
        ResponseEntity<KrameriusProcess[]> response = restTemplate.exchange(url, HttpMethod.GET, authHttpEntity, KrameriusProcess[].class);
        List<KrameriusProcess> krameriusProcesses = Arrays.asList(Objects.requireNonNull(response.getBody()));
        krameriusProcesses.forEach(p -> p.generateLogUrl(krameriusHost));
        return krameriusProcesses;
    }

    public void stopProcess(String uuid) {
        String url = krameriusHost + CLIENT_API_V4 + "/processes/" + uuid + "?stop";
        restTemplate.exchange(url, HttpMethod.PUT, authHttpEntity, KrameriusProcess.class);
    }

    public void removeProcessLog(String uuid) {
        String url = krameriusHost + CLIENT_API_V4 + "/processes/" + uuid;
        restTemplate.exchange(url, HttpMethod.DELETE, authHttpEntity, JsonObject.class);
    }

    public List<VC> getVirtualCollections() {
        String vcFetchUrl = krameriusHost + CLIENT_API_V5 + "/vc";
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(vcFetchUrl, VC[].class)));
    }

    public void generateAndDownloadPDF(Map<String, String> params, String outFilePath) throws IOException {
        String url = krameriusHost + CLIENT_API_V5 + "/pdf/parent";
        url = ApiConfiguration.buildUri(url, params);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, authHttpEntity, byte[].class);
        Files.write(Paths.get(outFilePath), Objects.requireNonNull(response.getBody()));
    }

    class Parameters {
        private final List<String> parameters; // {"parameters":["first","second","third"]}
        Parameters(String... parameters) {
            this.parameters = new ArrayList<>(Arrays.asList(parameters));
        }
    }
}
