package cz.mzk.fofola.model.doc;

import cz.mzk.fofola.constants.solr.SolrField;
import lombok.Getter;
import org.apache.solr.common.SolrDocumentList;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SolrDocument {

    private String uuid;
    private String rootTitle;
    private String visibility;
    private String model;
    private String rootPid;
    private List<String> parentPids;
    private Date modifiedDate;
    private String dcTitle;
    private List<Integer> relsExtIndex;
    private Boolean dnnt;
    private List<String> dnntLabels;

    private final SimpleDateFormat toFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getAccessibility() {
        return visibility;
    }

    public String getRootTitle() {
        if (rootTitle == null) return null;
        if (rootTitle.length() > 255) {
            rootTitle = rootTitle.substring(0, 255);
        }
        return rootTitle;
    }

    public String getModifiedDate() {
        if (modifiedDate == null) return null;
        else return toFmt.format(modifiedDate);
    }

    @SuppressWarnings("unchecked")
    private SolrDocument(org.apache.solr.common.SolrDocument originDoc) {
        uuid = (String) originDoc.getFieldValue(SolrField.UUID);
        model = (String) originDoc.getFieldValue(SolrField.MODEL);
        dcTitle = (String) originDoc.getFieldValue(SolrField.TITLE);
        rootPid = (String) originDoc.getFieldValue(SolrField.ROOT_PID);
        rootTitle = (String) originDoc.getFieldValue(SolrField.ROOT_TITLE);
        visibility = (String) originDoc.getFieldValue(SolrField.ACCESSIBILITY);
        modifiedDate = (Date) originDoc.getFieldValue(SolrField.MODIFIED_DATE);
        parentPids = (List<String>) originDoc.getFieldValue(SolrField.PARENT_PID);
        relsExtIndex = (List<Integer>) originDoc.getFieldValue(SolrField.RELS_EXT_INDEX);
        dnnt = (Boolean) originDoc.getFieldValue(SolrField.DNNT_FLAG);
        dnntLabels = (List<String>) originDoc.getFieldValue(SolrField.DNNT_LABELS);
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
