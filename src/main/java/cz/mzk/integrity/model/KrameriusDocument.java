package cz.mzk.integrity.model;

public class KrameriusDocument {

    private String uuid;
    private boolean isIndexed;
    private boolean isStored;

    private String accessibilityInSolr;
    private String accessibilityInFedora;

    private String model;
    private String rootTitle;

    private String imgUrl;
    private String solrModifiedDate;
    private String fedoraModifiedDate;


    public KrameriusDocument(String uuid) {
        this.uuid = uuid;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public void setStored(boolean stored) {
        isStored = stored;
    }

    public void setAccessibilityInSolr(String accessibility) {
        this.accessibilityInSolr = accessibility;
    }

    public void setAccessibilityInFedora(String accessibility) {
        this.accessibilityInFedora = accessibility;
    }

    public String getAccessibilityInSolr() {
        return accessibilityInSolr;
    }

    public String getAccessibilityInFedora() {
        return accessibilityInFedora;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public boolean isStored() {
        return isStored;
    }

    public String getUuid() {
        return uuid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setSolrModifiedDate(String solrModifiedDate) {
        this.solrModifiedDate = solrModifiedDate;
    }

    public void setFedoraModifiedDate(String fedoraModifiedDate) {
        this.fedoraModifiedDate = fedoraModifiedDate;
    }
}
