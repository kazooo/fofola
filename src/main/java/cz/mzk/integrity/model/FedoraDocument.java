package cz.mzk.integrity.model;

public class FedoraDocument {

    private String uuid;
    private String accesibility;
    private String model;
    private String imageUrl;

    public FedoraDocument(String uuid) {
        this.uuid = uuid;
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
}
