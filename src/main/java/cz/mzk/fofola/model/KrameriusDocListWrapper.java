package cz.mzk.fofola.model;

import java.util.ArrayList;
import java.util.List;

public class KrameriusDocListWrapper {

    private List<UuidStateResponse> krameriusDocs;

    public KrameriusDocListWrapper() {
        this.krameriusDocs = new ArrayList<>();
    }

    public List<UuidStateResponse> getKrameriusDocs() {
        return krameriusDocs;
    }

    public void setKrameriusDocs(List<UuidStateResponse> docs) {
        this.krameriusDocs = docs;
    }

    public void add(UuidStateResponse doc) {
        this.krameriusDocs.add(doc);
    }

    public int size() {
        return krameriusDocs.size();
    }

    public boolean isEmpty() {
        return krameriusDocs.isEmpty();
    }

    public void removeKrameriusDoc(int i) {
        this.krameriusDocs.remove(i);
    }
}
