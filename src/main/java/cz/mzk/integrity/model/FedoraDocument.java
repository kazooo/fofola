package cz.mzk.integrity.model;

import java.util.ArrayList;
import java.util.List;

public class FedoraDocument {

    private String uuid;
    private String accesibility;
    private String model;
    private String imageUrl;
    private List<String> childs;
    private String modifiedDateStr;

    public FedoraDocument(String uuid) {
        this.uuid = uuid;
        this.childs = new ArrayList<>();
    }

    public void setAccesibility(String accesibility) {
        this.accesibility = accesibility;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccesibility() {
        return accesibility;
    }

    public String getUuid() {
        return uuid;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addChild(String uuid) {
        this.childs.add(uuid);
    }

    public List<String> getChilds() {
        return childs;
    }

    public void setModifiedDateStr(String modifiedDateStr) {
        this.modifiedDateStr = modifiedDateStr;
    }

    public String getModifiedDateStr() {
        return modifiedDateStr;
    }
}
