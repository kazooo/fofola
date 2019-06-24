package cz.mzk.integrity.model;

public class KrameriusDocument {

    private String uuid;
    private boolean isIndexed;

    public KrameriusDocument(String uuid) {
        this.uuid = uuid;
    }

    public KrameriusDocument(String uuid, boolean isIndexed) {
        this.uuid = uuid;
        this.isIndexed = isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public String getUuid() {
        return uuid;
    }
}
