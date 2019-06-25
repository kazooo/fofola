package cz.mzk.integrity.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cz.mzk.integrity.model.DocListWrapper;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.researcher.UuidResearcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    private final UuidResearcher researcher;

    public HomeController(UuidResearcher researcher) {
        this.researcher = researcher;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/examine_pid")
    public String examineForm(HttpServletRequest request) {

        DocListWrapper krameriusDocs = getKrameriusDocs(request);

        if (krameriusDocs.size() < 1) {
            setKrameriusDocs(request, krameriusDocs);
        }

        return "examine";
    }

    @PostMapping("/examine_pid")
    public String examinePID(@RequestParam(name = "uuid", required = false) String uuid,
                             HttpServletRequest request,
                             @RequestParam(value = "file", required = false) MultipartFile file) {


        DocListWrapper krameriusDocs = getKrameriusDocs(request);

        if (file != null && !file.isEmpty()) {
            try {
                List<KrameriusDocument> kDocs = krameriusDocsForUuidsFromFile(file);
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

        setKrameriusDocs(request, krameriusDocs);

        return "redirect:/examine_pid";
    }

    private KrameriusDocument fillKrameriusDoc(KrameriusDocument krameriusDoc) {
        krameriusDoc = researcher.fillKrameriusDoc(krameriusDoc);
        return krameriusDoc;
    }

    private List<KrameriusDocument> krameriusDocsForUuidsFromFile(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String uuid;
        List<KrameriusDocument> result = new ArrayList<>();
        while ((uuid = br.readLine()) != null) {
            result.add(fillKrameriusDoc(new KrameriusDocument(uuid)));
        }
        return result;
    }

    private DocListWrapper getKrameriusDocs(HttpServletRequest request) {
        DocListWrapper krameriusDocs = (DocListWrapper) request.getSession().getAttribute("krameriusDocs");
        if (krameriusDocs == null) {
            krameriusDocs = new DocListWrapper();
        }
        return krameriusDocs;
    }

    private void setKrameriusDocs(HttpServletRequest request, DocListWrapper docs) {
        request.getSession().setAttribute("krameriusDocs", docs);
    }
}
