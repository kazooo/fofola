package cz.mzk.integrity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;


@org.springframework.data.solr.core.mapping.SolrDocument(collection = "kramerius")
public class SolrDocument {

    public final static String ID = "PID";
    public final static String ROOT_TITLE = "root_title";
    public final static String VISIBILITY = "dostupnost";
    public final static String MODEL = "fedora.model";

    @Id
    @Indexed(name = ID, type = "string")
    private String uuid;

    @Indexed(name = ROOT_TITLE, type = "string")
    private String rootTitle;

    @Indexed(name = VISIBILITY, type = "string")
    private String visibility;

    @Indexed(name = MODEL, type = "string")
    private String model;

    public String getAccessibility() {
        return visibility;
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
