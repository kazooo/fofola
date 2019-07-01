package cz.mzk.integrity.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cz.mzk.integrity.model.KrameriusDocListWrapper;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.service.AsynchronousService;
import cz.mzk.integrity.service.KrameriusApiCommunicator;
import cz.mzk.integrity.researcher.UuidResearcher;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    private final UuidResearcher researcher;
    private final KrameriusApiCommunicator krameriusApi;
    private final SolrCommunicator solrCommunicator;
    private final AsynchronousService asynchronousService;

    public HomeController(UuidResearcher researcher,
                          KrameriusApiCommunicator krameriusApi,
                          SolrCommunicator solrCommunicator,
                          AsynchronousService asynchronousService) {
        this.researcher = researcher;
        this.krameriusApi = krameriusApi;
        this.solrCommunicator = solrCommunicator;
        this.asynchronousService = asynchronousService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/examine_pid")
    public String examineForm(HttpServletRequest request) {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request);

        if (krameriusDocs.size() < 1) {
            insertDocsIntoSession(request, krameriusDocs);
        }

        return "examine";
    }

    @PostMapping("/examine_pid")
    public String examinePID(@RequestParam(name = "uuid", required = false) String uuid,
                             HttpServletRequest request,
                             @RequestParam(value = "file", required = false) MultipartFile file) {


        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request);

        if (file != null && !file.isEmpty()) {
            try {
                List<KrameriusDocument> kDocs = krameriusDocsForUuidsFromFile(file, true);
                for (KrameriusDocument doc : kDocs) {
                    krameriusDocs.add(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (uuid != null && !uuid.isEmpty()) {
            KrameriusDocument krameriusDoc = fillKrameriusDoc(new KrameriusDocument(uuid));
            krameriusDocs.add(krameriusDoc);
        }

        insertDocsIntoSession(request, krameriusDocs);

        return "redirect:/examine_pid";
    }

    @GetMapping("/change_rights")
    public String changeRightsForm(HttpServletRequest request) {
        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request);

        if (krameriusDocs.size() < 1) {
            insertDocsIntoSession(request, krameriusDocs);
        }
        return "change_rights";
    }

    @PostMapping("/change_rights")
    public String changeRightsList(@RequestParam(name = "uuid", required = false) String uuid,
                               HttpServletRequest request,
                               @RequestParam(value = "file", required = false) MultipartFile file) {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request);

        if (file != null && !file.isEmpty()) {
            try {
                List<KrameriusDocument> kDocs = krameriusDocsForUuidsFromFile(file, false);
                for (KrameriusDocument doc : kDocs) {
                    krameriusDocs.add(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (uuid != null && !uuid.isEmpty()) {
            KrameriusDocument krameriusDoc = fillKrameriusDoc(new KrameriusDocument(uuid));
            krameriusDocs.add(krameriusDoc);
        }

        insertDocsIntoSession(request, krameriusDocs);

        return "redirect:/change_rights";
    }

    @PostMapping(value = "/change_rights", params="action=private")
    public String makePrivate(HttpServletRequest request) throws Exception {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request);
        List<KrameriusDocument> docs = krameriusDocs.getKrameriusDocs();

        for (KrameriusDocument d : docs) {

            logger.info("private " + d.getUuid());
            krameriusApi.makePrivate(d.getUuid());
        }

        insertDocsIntoSession(request, null);
        return "redirect:/change_rights";
    }

    @PostMapping(value = "/change_rights", params="action=public")
    public String makePublic(HttpServletRequest request) throws Exception {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request);
        List<KrameriusDocument> docs = krameriusDocs.getKrameriusDocs();

        for (KrameriusDocument d : docs) {

            logger.info("public " + d.getUuid());
            krameriusApi.makePublic(d.getUuid());
        }

        insertDocsIntoSession(request, null);
        return "redirect:/change_rights";
    }

    @GetMapping("/check_solr_integrity")
    public String checkSolrIntegrity(Model model) {

        Map<String, Long> modelCount = solrCommunicator.facetSolrDocByModels();
        model.addAttribute("modelCount", modelCount);

        asynchronousService.runSolrChecking("periodical", 10);
        return "check_solr_integrity";
    }

    private KrameriusDocument fillKrameriusDoc(KrameriusDocument krameriusDoc) {
        krameriusDoc = researcher.fillKrameriusDoc(krameriusDoc);
        return krameriusDoc;
    }

    private List<KrameriusDocument> krameriusDocsForUuidsFromFile(MultipartFile file, boolean examine) throws IOException {
        List<KrameriusDocument> result = new ArrayList<>();
        List<String> uuids = uuidsFromFile(file);
        for (String uuid : uuids) {
            KrameriusDocument doc = new KrameriusDocument(uuid);
            if (examine) {
                doc = fillKrameriusDoc(doc);
            }
            result.add(doc);
        }
        return result;
    }

    private List<String> uuidsFromFile(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String uuid;
        List<String> uuidList = new ArrayList<>();
        while ((uuid = br.readLine()) != null) {
            uuidList.add(uuid);
        }
        return uuidList;
    }

    private KrameriusDocListWrapper extractDocsFromSession(HttpServletRequest request) {
        KrameriusDocListWrapper krameriusDocs = (KrameriusDocListWrapper) request.getSession().getAttribute("krameriusDocs");
        if (krameriusDocs == null) {
            krameriusDocs = new KrameriusDocListWrapper();
        }
        return krameriusDocs;
    }

    private void insertDocsIntoSession(HttpServletRequest request, KrameriusDocListWrapper docs) {
        request.getSession().setAttribute("krameriusDocs", docs);
    }
}
