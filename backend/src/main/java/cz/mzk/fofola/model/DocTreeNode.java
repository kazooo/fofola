package cz.mzk.fofola.model;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
public class DocTreeNode {

    public String name;
    public String uuid;
    public String model;
    public String stored;
    public String indexed;
    public String linkInRelsExt;
    public String visibilitySolr;
    public String visibilityFedora;
    public String imageUrl;
    public boolean hasProblem;
    public List<DocTreeNode> children;
    public boolean hasProblematicChild;

    public DocTreeNode(String u) {
        uuid = u;
        children = new ArrayList<>();
        linkInRelsExt = "true"; // for root node
    }

    public void addChild(DocTreeNode child) {
        children.add(child);
        hasProblematicChild = child.hasProblem;
    }

    public void checkProblems() {
        if (visibilityFedora == null || visibilitySolr == null) {
            hasProblem = true;
        } else if (!visibilityFedora.equals(visibilitySolr)) {
            hasProblem = true;
        }
        if (linkInRelsExt.equals("false")) {
            hasProblem = true;
        }
        if (stored.equals("false")) {
            hasProblem = true;
        }
        if (indexed.equals("false")) {
            hasProblem = true;
        }
        if (model != null && model.equals("page") && imageUrl.equals("no_image")) {
            hasProblem = true;
        }
        if (model != null && model.equals("page")) {
            hasProblematicChild = hasProblem;
        }
    }
}
