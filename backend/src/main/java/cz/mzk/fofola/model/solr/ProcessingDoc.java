package cz.mzk.fofola.model.solr;

import cz.mzk.fofola.utils.solr.SolrDocField;
import cz.mzk.fofola.utils.solr.SolrDocId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SolrDocId("pid")
public class ProcessingDoc {

    @SolrDocField("pid")
    private String pid;

    @SolrDocField("source")
    private String source;

    @SolrDocField("type")
    private String type;

    @SolrDocField("model")
    private String model;

    @SolrDocField("targetPid")
    private String targetPid;

    @SolrDocField("relation")
    private String relation;
}
