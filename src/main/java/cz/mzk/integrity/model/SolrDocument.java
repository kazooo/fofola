package cz.mzk.integrity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;


@org.springframework.data.solr.core.mapping.SolrDocument(collection = "kramerius")
public class SolrDocument {

    @Id
    @Indexed(name = "PID", type = "string")
    private String uuid;

    @Indexed(name = "root_title", type = "string")
    private String rootTitle;

    @Indexed(name = "dostupnost", type = "string")
    private String accessibility;

    @Indexed(name = "fedora.model", type = "string")
    private String model;

    public String getAccessibility() {
        return accessibility;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public String getModel() {
        return model;
    }
}
