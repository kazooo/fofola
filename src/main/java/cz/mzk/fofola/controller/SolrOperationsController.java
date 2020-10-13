package cz.mzk.fofola.controller;

import cz.mzk.fofola.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.Map;


@Controller
@Slf4j
public class SolrOperationsController {

    @MessageMapping("/remove-websocket")
    public void removeTreeFromSolr(Map<String, String> params) throws IOException, SolrServerException {
        String rootUuid = params.get("root_uuid");
        String solrHost = params.get("solr_host");
        log.info("Remove root uuid: " + rootUuid + " host: " + solrHost);
        SolrRecordEraser recordEraser = new SolrRecordEraser(solrHost, 1500, false);
        recordEraser.archiveAndEraseWithChildren(rootUuid);
        recordEraser.close();
    }

    @MessageMapping("/transfer-websocket")
    public void transferTreeBetweenSolrs(Map<String, String> params) throws IOException, SolrServerException {
        String srcSolrHost = params.get("src_solr_host");
        String dstSolrHost = params.get("dst_solr_host");
        String rootUuid = params.get("root_uuid");
        log.info("Transfer root uuid: " + rootUuid +
                        " source host: " + srcSolrHost +
                        " dest host: " + dstSolrHost);
        SolrRecordTransporter recordTransporter = new SolrRecordTransporter(
                srcSolrHost, dstSolrHost, 1000, false
        );
        recordTransporter.transport(rootUuid);
        recordTransporter.close();
    }
}
