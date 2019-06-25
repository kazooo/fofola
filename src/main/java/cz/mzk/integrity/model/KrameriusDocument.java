package cz.mzk.integrity.model;

public class KrameriusDocument {

    private String uuid;
    private boolean isIndexed;
    private boolean isStored;

    private String accessibilityInSolr;
    private String accessibilityInFedora;

    private String model;


    public KrameriusDocument(String uuid) {
        this.uuid = uuid;
    }

    public KrameriusDocument(String uuid, boolean isIndexed) {
        this.uuid = uuid;
        this.isIndexed = isIndexed;
    }

    public KrameriusDocument(String uuid, boolean isIndexed, boolean isStored) {
        this.uuid = uuid;
        this.isIndexed = isIndexed;
        this.isStored = isStored;
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
}
