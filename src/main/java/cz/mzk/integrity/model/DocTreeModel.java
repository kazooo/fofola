package cz.mzk.integrity.model;

import java.util.ArrayList;
import java.util.List;

public class DocTreeModel {

    public String name;
    public List<DocTreeModel> children;
    public String uuid;
    public String model;
    public String stored;
    public String visibilitySolr;
    public String visibilityFedora;
    public String imageUrl;

    public DocTreeModel(String name) {
        this.name = name;
    }

    public void setChildren(List<DocTreeModel> children) {
        this.children = children;
    }

    public void addChild(DocTreeModel child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setVisibilityFedora(String visibilityFedora) {
        this.visibilityFedora = visibilityFedora;
    }

    public void setVisibilitySolr(String visibilitySolr) {
        this.visibilitySolr = visibilitySolr;
    }

    public void setStored(String stored) {
        this.stored = stored;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
