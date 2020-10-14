package cz.mzk.fofola.controller;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.configuration.StartupApplicationListener;
import cz.mzk.fofola.model.vc.VC;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Controller
@Slf4j
@AllArgsConstructor
public class PageController {

    private final FofolaConfiguration fofolaConfiguration;

    @GetMapping("/")
    public String home(Model model) {
        log.info("Entry Fofola home page.");
        model.addAttribute("up_time", StartupApplicationListener.appStartupTime);
        return "index";
    }

    @GetMapping("/check-uuid")
    public String getCheckUuidPage() {
        log.info("Entry uuid checking section.");
        return "check-uuid";
    }

    @GetMapping("/change-access")
    public String getChangeVisibilityPage() {
        log.info("Entry uuid checking section.");
        return "change-access";
    }

    @GetMapping("/reindex")
    public String getReindexPage() {
        log.info("Entry reindexation section.");
        return "reindex";
    }

    @GetMapping("/link_vc")
    public String getVcLinkingPage(Model model) {
        log.info("Entry VC linking section.");
        List<VC> vcList = getAllVC(fofolaConfiguration.getKrameriusHost());
        Map<String, String> vcNameUuid = mapAndSortVCs(vcList);
        model.addAttribute("vcList", vcNameUuid);
        return "link-vc";
    }

    @GetMapping("/perio-parts-publish")
    public String getPerioPartsPublishingPage() {
        log.info("Entry private periodical parts publishing section.");
        return "perio-parts-publishing";
    }

    @GetMapping("/link-donator")
    public String getDonatorLinkingPage() {
        log.info("Entry donator linking section.");
        return "link-donator";
    }

    @GetMapping("/img-editing")
    public String getImgEditionPage() {
        log.info("Entry image editing section.");
        return "img-editing";
    }

    @GetMapping("/k-processes")
    public String getProcessControlPage() {
        log.info("Entry process control section.");
        return "k-processes";
    }

    @GetMapping("/tree")
    public String home() {
        log.info("Entry document tree section.");
        return "doc-tree";
    }

    @GetMapping("/check-donator")
    public String getCheckDonatorPage(Model model) {
        log.info("Entry donator checking section.");
        List<VC> vcList = getAllVC(fofolaConfiguration.getKrameriusHost());
        Map<String, String> vcNameUuid = mapAndSortVCs(vcList);
        model.addAttribute("vcList", vcNameUuid);
        return "check-donator";
    }

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
