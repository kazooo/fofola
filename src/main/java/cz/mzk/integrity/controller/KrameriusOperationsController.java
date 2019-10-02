package cz.mzk.integrity.controller;

import com.google.gson.Gson;
import cz.mzk.integrity.model.KrameriusDocListWrapper;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.researcher.UuidResearcher;
import cz.mzk.integrity.service.IpLogger;
import cz.mzk.integrity.service.KrameriusApiCommunicator;
import org.aspectj.weaver.patterns.ExactAnnotationFieldTypePattern;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Controller
public class KrameriusOperationsController {

    private static final Logger logger = Logger.getLogger(KrameriusOperationsController.class.getName());
    private static final Gson gson = new Gson();

    private final UuidResearcher researcher;
    private final KrameriusApiCommunicator krameriusApi;
    private String EXAMINE_FLAG = "docsToExamine";
    private String CHANGE_RIGHTS_FLAG = "docsToChange";

    public KrameriusOperationsController(UuidResearcher researcher,
                          KrameriusApiCommunicator krameriusApi) {
        this.researcher = researcher;
        this.krameriusApi = krameriusApi;
    }

    @GetMapping("/check_uuid")
    public String examineForm(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry uuid checking section.");
        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request, EXAMINE_FLAG);
        if (krameriusDocs.size() < 1) {
            insertDocsIntoSession(request, krameriusDocs, EXAMINE_FLAG);
        }
        return "check_uuid";
    }

    @PostMapping("/check_uuid")
    public String examinePID(@RequestParam(name = "uuid", required = false) String uuid,
                             HttpServletRequest request,
                             @RequestParam(value = "file", required = false) MultipartFile file) {


        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request, EXAMINE_FLAG);
        String logMsg = "Checking: ";
        if (file != null && !file.isEmpty()) {
            logMsg += "file";
            try {
                List<KrameriusDocument> kDocs = krameriusDocsForUuidsFromFile(file, true);
                for (KrameriusDocument doc : kDocs) {
                    krameriusDocs.add(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (uuid != null && !uuid.isEmpty()) {
            logMsg += uuid;
            KrameriusDocument krameriusDoc = fillKrameriusDoc(new KrameriusDocument(uuid));
            krameriusDocs.add(krameriusDoc);
        }

        IpLogger.logIp(request.getRemoteAddr(), logMsg);
        insertDocsIntoSession(request, krameriusDocs, EXAMINE_FLAG);

        return "redirect:/check_uuid";
    }

    @PostMapping(value = "/check_uuid", params="action=clear")
    public String clearListExaminedUuids(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Clear checking section.");
        clearDocsInSession(request, EXAMINE_FLAG);
        return "redirect:/check_uuid";
    }

    @GetMapping("/change_rights")
    public String changeRightsForm(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry visibility changing section.");
        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request, CHANGE_RIGHTS_FLAG);

        if (krameriusDocs.size() < 1) {
            insertDocsIntoSession(request, krameriusDocs, CHANGE_RIGHTS_FLAG);
        }
        return "change_rights";
    }

    @PostMapping("/change_rights")
    public String changeRightsList(@RequestParam(name = "uuid", required = false) String uuid,
                                   HttpServletRequest request,
                                   @RequestParam(value = "file", required = false) MultipartFile file) {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request, CHANGE_RIGHTS_FLAG);
        String logMsg = "Uploadnig: ";
        if (file != null && !file.isEmpty()) {
            logMsg += "file";
            try {
                List<KrameriusDocument> kDocs = krameriusDocsForUuidsFromFile(file, false);
                for (KrameriusDocument doc : kDocs) {
                    krameriusDocs.add(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (uuid != null && !uuid.isEmpty()) {
            logMsg += uuid;
            KrameriusDocument krameriusDoc = fillKrameriusDoc(new KrameriusDocument(uuid));
            krameriusDocs.add(krameriusDoc);
        }

        IpLogger.logIp(request.getRemoteAddr(), logMsg);
        insertDocsIntoSession(request, krameriusDocs, CHANGE_RIGHTS_FLAG);

        return "redirect:/change_rights";
    }

    @PostMapping(value = "/change_rights", params="action=private")
    public String makePrivate(HttpServletRequest request) throws Exception {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request, CHANGE_RIGHTS_FLAG);
        List<KrameriusDocument> docs = krameriusDocs.getKrameriusDocs();

        for (KrameriusDocument d : docs) {
            krameriusApi.makePrivate(d.getUuid());
        }

        IpLogger.logIp(request.getRemoteAddr(), "Change visibility: private.");
        insertDocsIntoSession(request, null, CHANGE_RIGHTS_FLAG);
        return "redirect:/change_rights";
    }

    @PostMapping(value = "/change_rights", params="action=public")
    public String makePublic(HttpServletRequest request) throws Exception {

        KrameriusDocListWrapper krameriusDocs = extractDocsFromSession(request, CHANGE_RIGHTS_FLAG);
        List<KrameriusDocument> docs = krameriusDocs.getKrameriusDocs();

        for (KrameriusDocument d : docs) {
            IpLogger.logIp(request.getRemoteAddr(), "Change visibility: public.");
            krameriusApi.makePublic(d.getUuid());
        }

        insertDocsIntoSession(request, null, CHANGE_RIGHTS_FLAG);
        return "redirect:/change_rights";
    }

    @PostMapping(value = "/change_rights", params="action=clear")
    public String clearListTOChangeRight(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Clear visibility changing section.");
        clearDocsInSession(request, CHANGE_RIGHTS_FLAG);
        return "redirect:/change_rights";
    }

    @GetMapping("/reindex")
    public String getReindexPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry reindexation section.");
        return "reindex";
    }

    @MessageMapping("/reindex-websocket")
    public void loadDataToReindex(@Payload String uuid, SimpMessageHeaderAccessor ha) throws Exception {
        Object ipAdress = ha.getSessionAttributes().get("IP");
        IpLogger.logIp(ipAdress.toString(), "Reindex: " + uuid);
        krameriusApi.reindex(uuid);
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

    private KrameriusDocListWrapper extractDocsFromSession(HttpServletRequest request, String flag) {
        KrameriusDocListWrapper krameriusDocs = (KrameriusDocListWrapper) request.getSession().getAttribute(flag);
        if (krameriusDocs == null) {
            krameriusDocs = new KrameriusDocListWrapper();
        }
        return krameriusDocs;
    }

    private void insertDocsIntoSession(HttpServletRequest request, KrameriusDocListWrapper docs, String flag) {
        request.getSession().setAttribute(flag, docs);
    }

    private void clearDocsInSession(HttpServletRequest request, String flag) {
        request.getSession().setAttribute(flag, null);
    }
}
