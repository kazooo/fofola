package cz.mzk.integrity.model;

import java.util.ArrayList;
import java.util.List;

public class DocTreeModel {

    public String name;
    public List<DocTreeModel> children;
    public String uuid;
    public String model;
    public String stored;
    public String indexed;
    public String visibilitySolr;
    public String visibilityFedora;
    public String imageUrl;
    public boolean hasProblem;
    public boolean hasProblematicChild;

    public DocTreeModel(String name) {
        this.name = name;
        this.hasProblem = false;
        this.indexed = "true";
        this.hasProblematicChild = false;
    }

    public void setChildren(List<DocTreeModel> children) {
        this.children = children;
    }

    public void addChild(DocTreeModel child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
        if (child.hasProblem) {
            this.hasProblematicChild = true;
        }
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

    public void setIndexed(String indexed) {
        this.indexed = indexed;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void checkProblems() {
        if (!this.visibilityFedora.equals(this.visibilitySolr)) {
            this.hasProblem = true;
        }
        if (this.stored.equals("false")) {
            this.hasProblem = true;
        }
        if (this.model != null && this.model.equals("page") && this.imageUrl.equals(UuidProblem.NO_IMAGE)) {
            this.hasProblem = true;
        }
        if (this.model != null && this.model.equals("page")) {
            this.hasProblematicChild = this.hasProblem;
        }
    }
}
