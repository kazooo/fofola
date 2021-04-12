package cz.mzk.fofola.controller;

import cz.mzk.fofola.configuration.StartupApplicationListener;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.api.KrameriusApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;


@Controller
@Slf4j
@AllArgsConstructor
public class PageController {

    private final KrameriusApi krameriusApi;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("up_time", StartupApplicationListener.appStartupTime);
        return "index";
    }

    @GetMapping("/check-uuid")
    public String getCheckUuidPage() {
        return "check-uuid";
    }

    @GetMapping("/change-access")
    public String getChangeVisibilityPage() {
        return "change-access";
    }

    @GetMapping("/reindex")
    public String getReindexPage() {
        return "reindex";
    }

    @GetMapping("/delete")
    public String getDeletePage() {
        return "delete";
    }

    @GetMapping("/solr-query")
    public String getSolrQueryPage() {
        return "solr-response";
    }

    @GetMapping("/link_vc")
    public String getVcLinkingPage(Model model) {
        Map<String, String> vcNameUuid = getSortedVirtualCollection();
        model.addAttribute("vcList", vcNameUuid);
        return "link-vc";
    }

    @GetMapping("/perio-parts-publish")
    public String getPerioPartsPublishingPage() {
        return "perio-parts-publishing";
    }

    @GetMapping("/link-donator")
    public String getDonatorLinkingPage() {
        return "link-donator";
    }

    @GetMapping("/img-editing")
    public String getImgEditionPage() {
        return "img-editing";
    }

    @GetMapping("/k-processes")
    public String getProcessControlPage() {
        return "k-processes";
    }

    @GetMapping("/tree")
    public String home() {
        return "doc-tree";
    }

    @GetMapping("/pdf")
    public String getPDFGeneratingPage() {
        return "pdf";
    }

    @GetMapping("/check-donator")
    public String getCheckDonatorPage(Model model) {
        Map<String, String> vcNameUuid = getSortedVirtualCollection();
        model.addAttribute("vcList", vcNameUuid);
        return "check-donator";
    }

    private Map<String, String> getSortedVirtualCollection() {
        List<VC> vcList = krameriusApi.getVirtualCollections();
        Map<String, String> vcNameUuid = new TreeMap<>();
        vcList.forEach(vc -> vcNameUuid.put(vc.descs.cs, vc.pid));
        return vcNameUuid;
    }
}
