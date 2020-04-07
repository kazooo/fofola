package cz.mzk.fofola.controller;

import cz.mzk.fofola.service.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;


@Controller
public class SolrOperationsController {

    @GetMapping("/removeTree")
    public String getRemoveTreeFromSolrPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry Solr tree removing section.");
        return "remove_tree_from_solr";
    }

    @MessageMapping("/remove-websocket")
    public void removeTreeFromSolr(Map<String, String> params, SimpMessageHeaderAccessor ha)
            throws IOException, SolrServerException {
        String rootUuid = params.get("rootUuid");
        String solrHost = params.get("solrHost");
        IpLogger.logIp((String) ha.getSessionAttributes().get("IP"),
                "Remove root uuid: " + rootUuid + " host: " + solrHost);
        SolrRecordEraser recordEraser = new SolrRecordEraser(solrHost, 1500, false);
        recordEraser.archiveAndEraseWithChildren(rootUuid);
        recordEraser.close();
    }
}
