package cz.mzk.fofola.model;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class DocTreeModel {

    public String name;
    public List<DocTreeModel> children;
    public String uuid;
    public String model;
    public String stored;
    public String indexed;
    public String linkInRelsExt;
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
        this.linkInRelsExt = "true";
        this.stored = "false";
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

    public void setLinkInRelsExt(String linkInRelsExt) {
        this.linkInRelsExt = linkInRelsExt;
        checkProblems();
    }

    public void checkProblems() {
        if (this.visibilityFedora == null || this.visibilitySolr == null) {
            this.hasProblem = true;
        } else if (!this.visibilityFedora.equals(this.visibilitySolr)) {
            this.hasProblem = true;
        }
        if (this.linkInRelsExt.equals("false")) {
            this.hasProblem = true;
        }
        if (this.stored.equals("false")) {
            this.hasProblem = true;
        }
        if (this.model != null && this.model.equals("page") && this.imageUrl.equals("no_image")) {
            this.hasProblem = true;
        }
        if (this.model != null && this.model.equals("page")) {
            this.hasProblematicChild = this.hasProblem;
        }
    }
}
