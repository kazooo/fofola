package cz.mzk.integrity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;


@org.springframework.data.solr.core.mapping.SolrDocument(collection = "kramerius")
public class SolrDocument {

    @Id
    @Indexed(name = "PID", type = "string")
    public String uuid;

    @Indexed(name = "root_title", type = "string")
    public String rootTitle;

    @Indexed(name = "dostupnost", type = "string")
    public String accessibility;
}
