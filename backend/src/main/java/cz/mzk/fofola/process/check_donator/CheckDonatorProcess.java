package cz.mzk.fofola.process.check_donator;

import cz.mzk.fofola.constants.ModelName;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import cz.mzk.fofola.model.solr.ProcessingDoc;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.SolrRepository;
import cz.mzk.fofola.service.FileService;
import cz.mzk.fofola.utils.solr.SolrProcessingField;
import cz.mzk.fofola.utils.solr.SolrQueryBuilder;
import cz.mzk.fofola.utils.solr.SolrSearchField;
import org.apache.solr.client.solrj.SolrQuery;
import cz.mzk.fofola.model.process.Process;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CheckDonatorProcess extends Process {

    private final String vcId;
    private final String donator;
    private final CheckOption option;
    private final SolrRepository<SearchDoc> solrSearchRepository;
    private final SolrRepository<ProcessingDoc> solrProcessingRepository;

    public CheckDonatorProcess(final ProcessParams params) throws Exception {
        super(params);
        Map<String, ?> data = params.getData();

        vcId = (String) data.get("vcUuid");
        donator = (String) data.get("donator");
        option = CheckOption.valueOf((String) data.get("mode"));

        solrSearchRepository = params.getSolrSearchRepository();
        solrProcessingRepository = params.getSolrProcessingRepository();
    }

    @Override
    public TerminationReason process() throws Exception {
        String readyOutFileName = FileService.fileNameWithDateStampPrefix(donator + ".txt");
        File notReadyOutFile = FileService.getCheckDonatorOutputFile("not-ready-" + readyOutFileName);
        PrintWriter output = new PrintWriter(notReadyOutFile);

        SolrQuery query = createAllUuidsQuery();
        Consumer<SearchDoc> checkDonatorLogic = getCheckDonatorLogic(output);

        try {
            solrSearchRepository.paginateByCursor(query, checkDonatorLogic);
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

    private Consumer<SearchDoc> getCheckDonatorLogic(PrintWriter output) {
        Predicate<ProcessingDoc> logic =
                option == CheckOption.CHECK_HAS_DONATOR ? Objects::nonNull : Objects::isNull;

        return doc -> {
            String uuid = doc.getUuid();
            logger.info(uuid);
            SolrQuery query = createHasDonatorQuery(uuid);
            ProcessingDoc processingDoc = solrProcessingRepository.getFirstByQuery(query);
            if (logic.test(processingDoc)) {
                output.println(uuid);
            }
        };
    }

    private SolrQuery createAllUuidsQuery() {
        String query = SolrQueryBuilder.start()
                .is(SolrSearchField.IN_COLLECTIONS, vcId, true)
                .and()
                .not(SolrSearchField.MODEL, ModelName.PAGE, true)
                .build();
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addFilterQuery(SolrQueryBuilder.ROOT_ONLY_FILTER_QUERY);
        solrQuery.addField(SolrSearchField.UUID);
        return solrQuery;
    }

    private SolrQuery createHasDonatorQuery(String uuid) {
        String query = SolrQueryBuilder.start()
                .is(SolrProcessingField.SOURCE, uuid, true)
                .and()
                .is(SolrProcessingField.RELATION, "hasDonator", true)
                .and()
                .is(SolrProcessingField.TARGET, donator, true)
                .build();
        return new SolrQuery(query);
    }

    private void renameOutFile(File outFile, String newFileName) {
        String pathToDir = outFile.getParent();
        boolean success = outFile.renameTo(new File(pathToDir + "/" + newFileName));
        if (!success) {
            throw new IllegalStateException("Can't rename output file!");
        }
    }

    public enum CheckOption {
        CHECK_HAS_DONATOR,
        CHECK_HASNT_DONATOR;
    }
}
