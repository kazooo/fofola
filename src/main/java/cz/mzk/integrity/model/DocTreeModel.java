package cz.mzk.integrity.model;

import java.util.ArrayList;
import java.util.List;

public class DocTreeModel {

    public String name;
    public List<DocTreeModel> children;

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
}
