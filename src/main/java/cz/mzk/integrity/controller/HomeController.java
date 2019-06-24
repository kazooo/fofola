package cz.mzk.integrity.controller;

import java.util.logging.Logger;

import cz.mzk.integrity.model.DocListWrapper;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.researcher.UuidResearcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String examinePID(@RequestParam(name = "uuid") String uuid,
                             HttpServletRequest request) {

        logger.info("Examine document: " + uuid);

        KrameriusDocument krameriusDoc = new KrameriusDocument(uuid);
        krameriusDoc.setIndexed(researcher.isIndexed(uuid));

        DocListWrapper krameriusDocs = getKrameriusDocs(request);
        krameriusDocs.add(krameriusDoc);
        setKrameriusDocs(request, krameriusDocs);

        return "redirect:/examine_pid";
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
