package cz.mzk.fofola.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FedoraDocument {

    private String uuid;
    private String accesibility;
    private String model;
    private String imageUrl;
    private List<String> childs;
    private String modifiedDateStr;

    private final SimpleDateFormat fromFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final SimpleDateFormat toFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        try {
            Date d = fromFmt.parse(modifiedDateStr);
            modifiedDateStr = toFmt.format(d);
        } catch (ParseException ignored) {}
        this.modifiedDateStr = modifiedDateStr;
    }

    public String getModifiedDateStr() {
        return modifiedDateStr;
    }
}
