package cz.mzk.fofola.process.perio_parts_publishing;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.constants.AccessType;
import cz.mzk.fofola.constants.ModelName;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.SolrRepository;
import cz.mzk.fofola.service.UuidService;
import cz.mzk.fofola.utils.solr.SolrQueryBuilder;
import cz.mzk.fofola.utils.solr.SolrSearchField;
import org.apache.solr.client.solrj.SolrQuery;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class PerioPartsPublisher {

    private final KrameriusApi krameriusApi;
    private final SolrRepository<SearchDoc> solrRepository;
    private final Logger logger;

    public PerioPartsPublisher(
            final KrameriusApi krameriusApi,
            final SolrRepository<SearchDoc> solrRepository,
            final Logger logger
    ) {
        this.krameriusApi = krameriusApi;
        this.solrRepository = solrRepository;
        this.logger = logger;
    }

    public void checkPartsAndMakePublic(String rootUuid) {
        rootUuid = UuidService.makeUuid(rootUuid);
        try {
            final SearchDoc perioDoc = solrRepository.getByUuid(rootUuid);
            checkAndPublishDocsRecursively(perioDoc, "");
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    private boolean checkAndPublishDocsRecursively(final SearchDoc doc, final String indent) {
        final AtomicBoolean makePublic = new AtomicBoolean(false);
        final String uuid = doc.getUuid();
        final SolrQuery childQuery = createQueryForChildNodes(uuid);
        final Consumer<SearchDoc> publisherConsumer = solrDoc -> {
            final AccessType accessibility = solrDoc.getAccessibility();
            if (accessibility == AccessType.PUBLIC) {
                makePublic.set(true);
                logger.info(indent + "Public document detected, make its parent public too...");
            } else {
                boolean childArePublic = checkAndPublishDocsRecursively(solrDoc, "    " + indent);
                if (childArePublic) {
                    makePublic.set(true);
                }
            }
        };
        solrRepository.paginateByCursor(childQuery, publisherConsumer);
        if (makePublic.get()) {
            logger.info(indent + uuid + " make public!");
            krameriusApi.makePublic(uuid, "OBJECT");
        }
        return makePublic.get();
    }

    private SolrQuery createQueryForChildNodes(final String parentUuid) {
        final String queryStr = SolrQueryBuilder.start()
                .is(SolrSearchField.PARENT_UUID, parentUuid, true)
                .and()
                .not(SolrSearchField.UUID, parentUuid, true)
                .and()
                .not(SolrSearchField.MODEL, ModelName.PAGE, true)
                .build();
        SolrQuery query = new SolrQuery(queryStr);
        query.setFields(SolrSearchField.UUID, SolrSearchField.ACCESSIBILITY);
        return query;
    }
}
