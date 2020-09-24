package cz.mzk.fofola.service;

import cz.mzk.fofola.model.vc.VC;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class VCUtils {

    public static List<VC> getAllVC(String krameriusHost) {
        RestTemplate restTemplate = new RestTemplate();
        String vcFetchUrl = krameriusHost + "/search/api/v5.0/vc";
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(vcFetchUrl, VC[].class)));
    }

    public static Map<String, String> mapAndSortVCs(List<VC> vcs) {
        Map<String, String> vcNameUuid = new TreeMap<>();
        vcs.forEach(vc -> vcNameUuid.put(vc.descs.cs, vc.pid));
        return vcNameUuid;
    }
}
