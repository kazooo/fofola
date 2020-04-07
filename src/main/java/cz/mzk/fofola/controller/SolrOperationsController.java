package cz.mzk.fofola.controller;

import cz.mzk.fofola.service.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;


@Controller
public class SolrOperationsController {

    private static final Logger logger = Logger.getLogger(SolrOperationsController.class.getName());

    @GetMapping("/removeTree")
    public String getRemoveTreeFromSolrPage() {
        logger.info("Entry Solr tree removing section.");
        return "remove_tree_from_solr";
    }

    @MessageMapping("/remove-websocket")
    public void removeTreeFromSolr(Map<String, String> params) throws IOException, SolrServerException {
        String rootUuid = params.get("root_uuid");
        String solrHost = params.get("solr_host");
        logger.info("Remove root uuid: " + rootUuid + " host: " + solrHost);
        SolrRecordEraser recordEraser = new SolrRecordEraser(solrHost, 1500, false);
        recordEraser.archiveAndEraseWithChildren(rootUuid);
        recordEraser.close();
    }

    @GetMapping("/transferTree")
    public String getTransferTreePage() {
        logger.info("Entry Solr tree transfer section.");
        return "solr_record_transfer";
    }

    @MessageMapping("/transfer-websocket")
    public void transferTreeBetweenSolrs(Map<String, String> params) throws IOException, SolrServerException {
        String srcSolrHost = params.get("src_solr_host");
        String dstSolrHost = params.get("dst_solr_host");
        String rootUuid = params.get("root_uuid");
        logger.info("Transfer root uuid: " + rootUuid +
                        " source host: " + srcSolrHost +
                        " dest host: " + dstSolrHost);
        SolrRecordTransporter recordTransporter = new SolrRecordTransporter(
                srcSolrHost, dstSolrHost, 1000, false
        );
        recordTransporter.transport(rootUuid);
        recordTransporter.close();
    }
}
