package cz.mzk.fofola.model.solr;

import cz.mzk.fofola.constants.AccessType;
import cz.mzk.fofola.utils.solr.SolrDocField;
import cz.mzk.fofola.utils.solr.SolrDocId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SolrDocId("pid")
public class SearchDoc {

    @SolrDocField("pid")
    private String uuid;

    @SolrDocField("accessibility")
    private AccessType accessibility;

    @SolrDocField("model")
    private String model;

    @SolrDocField("title.search")
    private String title;

    @SolrDocField("root.pid")
    private String rootUuid;

    @SolrDocField("root.title")
    private String rootTitle;

    @SolrDocField("modified")
    private String modified;
}
