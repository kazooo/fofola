package cz.mzk.integrity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

import java.util.Date;
import java.util.List;


@org.springframework.data.solr.core.mapping.SolrDocument(collection = "kramerius")
public class SolrDocument {

    public final static String ID = "PID";
    public final static String PARENT_PID = "parent_pid";
    public final static String MODIFIED_DATE = "modified_date";
    public final static String ROOT_TITLE = "root_title";
    public final static String VISIBILITY = "dostupnost";
    public final static String MODEL = "fedora.model";
    public static final String ROOT_PID = "root_pid";
    public static final String DC_TITLE = "dc.title";
    public static final String RELS_EXT_INDEX = "rels_ext_index";

    @Id
    @Indexed(name = ID, type = "string")
    private String uuid;

    @Indexed(name = ROOT_TITLE, type = "string")
    private String rootTitle;

    @Indexed(name = VISIBILITY, type = "string")
    private String visibility;

    @Indexed(name = MODEL, type = "string")
    private String model;

    @Indexed(name = ROOT_PID, type = "string")
    private String rootPid;

    @Indexed(name = PARENT_PID, type = "multivalued")
    private List<String> parentPids;

    @Indexed(name = MODIFIED_DATE, type = "date")
    private Date modifiedDate;

    @Indexed(name = DC_TITLE, type = "string")
    private String dcTitle;

    @Indexed(name = RELS_EXT_INDEX, type = "multivalued")
    private List<Integer> relsExtIndex;

    public String getAccessibility() {
        return visibility;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRootTitle() {
        if (rootTitle.length() > 255) {
            rootTitle = rootTitle.substring(0, 255);
        }
        return rootTitle;
    }

    public String getModel() {
        return model;
    }

    public String getRootPid() {
        return rootPid;
    }

    public List<String> getParentPids() {
        return parentPids;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public String getDcTitle() { return dcTitle; }

    public Integer getRelsExtIndexForParent(String parentUuid) {
        int index = 0;  // looking for the right parent
        for(int i = 0; i < parentPids.size(); ++i){
            if (parentPids.get(i).contains(parentUuid)) {
                index = i;
            }
        }
        return relsExtIndex.get(index);
    }
}
