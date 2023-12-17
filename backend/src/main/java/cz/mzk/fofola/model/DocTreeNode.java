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
    public Boolean visibilitySolr;
    public Boolean visibilityFedora;
    public String imageUrl;
    public boolean hasProblem;
    public boolean hasProblematicChild;

    public DocTreeNode(String u) {
        uuid = u;
        linkInRelsExt = "true"; // for root node
    }

    public void checkProblems() {
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
