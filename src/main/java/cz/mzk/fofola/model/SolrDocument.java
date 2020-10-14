package cz.mzk.fofola.model;

import org.apache.solr.common.SolrDocumentList;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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

    private String uuid;
    private String rootTitle;
    private String visibility;
    private String model;
    private String rootPid;
    private List<String> parentPids;
    private Date modifiedDate;
    private String dcTitle;
    private List<Integer> relsExtIndex;

    private final SimpleDateFormat toFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getAccessibility() {
        return visibility;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRootTitle() {
        if (rootTitle == null) return null;
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

    public String getModifiedDate() {
        if (modifiedDate == null) return null;
        else return toFmt.format(modifiedDate);
    }

    public String getDcTitle() { return dcTitle; }

    @SuppressWarnings("unchecked")
    private SolrDocument(org.apache.solr.common.SolrDocument originDoc) {
        uuid = (String) originDoc.getFieldValue(ID);
        model = (String) originDoc.getFieldValue(MODEL);
        dcTitle = (String) originDoc.getFieldValue(DC_TITLE);
        rootPid = (String) originDoc.getFieldValue(ROOT_PID);
        rootTitle = (String) originDoc.getFieldValue(ROOT_TITLE);
        visibility = (String) originDoc.getFieldValue(VISIBILITY);
        modifiedDate = (Date) originDoc.getFieldValue(MODIFIED_DATE);
        parentPids = (List<String>) originDoc.getFieldValue(PARENT_PID);
        relsExtIndex = (List<Integer>) originDoc.getFieldValue(RELS_EXT_INDEX);
    }

    public static SolrDocument convert(org.apache.solr.common.SolrDocument originDoc) {
        if (originDoc == null) return null;
        else return new SolrDocument(originDoc);
    }

    public static List<SolrDocument> convert(SolrDocumentList docs) {
        if (docs == null) return Collections.emptyList();
        else {
            return docs.stream().map(SolrDocument::new).collect(Collectors.toList());
        }
    }
}
