package cz.mzk.fofola.process.check_donator;

import cz.mzk.fofola.api.fedora.FedoraApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.constants.solr.SolrField;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import cz.mzk.fofola.service.FileService;
import cz.mzk.fofola.service.SolrService;
import cz.mzk.fofola.service.XMLService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.w3c.dom.Document;
import cz.mzk.fofola.model.process.Process;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CheckDonatorProcess extends Process {

    private final String vcId;
    private final String donator;
    private final CheckOption option;
    private final SolrClient solrClient;
    private final FedoraApi fedoraApi;
    private final XMLService xmlService;

    public CheckDonatorProcess(ProcessParams params) throws Exception {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();

        vcId = (String) data.get("vcUuid");
        donator = (String) data.get("donator");
        option = CheckOption.valueOf((String) data.get("mode"));

        xmlService = new XMLService();
        solrClient = SolrService.buildClient(fofolaConfig.getSolrHost());
        fedoraApi = ApiConfiguration.getFedoraApi(fofolaConfig);
    }

    @Override
    public TerminationReason process() throws Exception {
        String readyOutFileName = FileService.fileNameWithDateStampPrefix(donator + ".txt");
        File notReadyOutFile = FileService.getCheckDonatorOutputFile("not-ready-" + readyOutFileName);
        PrintWriter output = new PrintWriter(notReadyOutFile);

        SolrQuery query = prepareQuery();
        Consumer<SolrDocument> checkDonatorLogic = getCheckDonatorLogic(output);

        try {
            SolrService.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                    query,
                    solrClient,
                    checkDonatorLogic,
                    1000);
        } catch (Exception e) {
            output.close();
            renameOutFile(notReadyOutFile, "terminated-" + readyOutFileName);
            throw e;
        }

        output.flush();
        output.close();
        renameOutFile(notReadyOutFile, readyOutFileName);
        return null;
    }

    private SolrQuery prepareQuery() {
        String query = SolrService.wrapQueryStr(SolrField.COLLECTION, vcId) +
                " AND NOT " + SolrService.wrapQueryStr(SolrField.MODEL, "page");
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addFilterQuery("{!frange l=1 u=1 v=eq(PID,root_pid)}"); // filter roots
        solrQuery.addField(SolrField.UUID);
        return solrQuery;
    }

    private Consumer<SolrDocument> getCheckDonatorLogic(PrintWriter output) {
        Predicate<List<Node>> logic =
                option == CheckOption.CHECK_HAS_DONATOR ? getHasDonatorCheckLogic() : getHasntDonatorCheckLogic();

        return solrDoc -> {
            String uuid = (String) solrDoc.getFieldValue(SolrField.UUID);
            logger.info(uuid);
            Document relsExt = fedoraApi.getRelsExt(uuid);
            try {
                List<Node> nodes = xmlService.getHasDonatorNodes(donator, relsExt);
                if (logic.test(nodes))
                    output.println(uuid);
            } catch (XPathExpressionException e) {
                throw new IllegalStateException("Can't parse XML donator nodes!");
            }
        };
    }

    private Predicate<List<Node>> getHasDonatorCheckLogic() {
        return nodes -> nodes.size() > 0;
    }

    private Predicate<List<Node>> getHasntDonatorCheckLogic() {
        return nodes -> nodes.size() == 0;
    }

    private void renameOutFile(File outFile, String newFileName) {
        String pathToDir = outFile.getParent();
        boolean success = outFile.renameTo(new File(pathToDir + "/" + newFileName));
        if (!success) throw new IllegalStateException("Can't rename output file!");
    }

    public enum CheckOption {
        CHECK_HAS_DONATOR,
        CHECK_HASNT_DONATOR;
    }
}
