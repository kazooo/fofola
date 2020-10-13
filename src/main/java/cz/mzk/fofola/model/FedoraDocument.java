package cz.mzk.fofola.model;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class FedoraDocument {

    private String uuid;
    private String model;
    private String title;
    private String imageUrl;
    private String accesibility;
    private List<String> childUuids;
    private String modifiedDateStr;

    private final SimpleDateFormat fromFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final SimpleDateFormat toFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FedoraDocument(String uuid) {
        this.uuid = uuid;
        this.childUuids = new ArrayList<>();
    }

    public void setModifiedDateStr(String modifiedDateStr) {
        try {
            Date d = fromFmt.parse(modifiedDateStr);
            modifiedDateStr = toFmt.format(d);
        } catch (ParseException ignored) {}
        this.modifiedDateStr = modifiedDateStr;
    }

    public void addChild(String child) {
        this.childUuids.add(child);
    }
}
