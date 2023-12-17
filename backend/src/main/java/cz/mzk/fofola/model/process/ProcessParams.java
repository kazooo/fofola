package cz.mzk.fofola.model.process;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.configuration.AppProperties;
import cz.mzk.fofola.model.solr.ProcessingDoc;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.process.ProcessEventNotifier;
import cz.mzk.fofola.repository.SolrRepository;
import lombok.Data;

import java.util.Map;

@Data
public class ProcessParams {

    private String id;
    private ProcessType type;
    private Map<String, Object> data;
    private AppProperties config;
    private ProcessEventNotifier eventNotifier;

    private KrameriusApi krameriusApi;
    private SugoApi sugoApi;
    private SolrRepository<SearchDoc> solrSearchRepository;
    private SolrRepository<ProcessingDoc> solrProcessingRepository;
}
