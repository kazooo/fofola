package cz.mzk.integrity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;


@org.springframework.data.solr.core.mapping.SolrDocument(collection = "kramerius")
public class SolrDocument {

    @Id
    @Indexed(name = "id", type = "string")  // TODO change 'id' to 'PID'
    private String uuid;

    @Indexed(name = "root_title", type = "string")
    private String rootTitle;
}
