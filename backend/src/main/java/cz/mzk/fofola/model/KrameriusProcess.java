package cz.mzk.fofola.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString(exclude = {"children"})
@EqualsAndHashCode
public class KrameriusProcess {

    String uuid; // without prefix "uuid:"
    String pid;  // internal process id
    String def;  // process type
    String state;
    String batchState;
    String name;
    String started;
    String planned;
    String finished;
    String userid;
    String userFirstname;
    String userSurname;
    List<KrameriusProcess> children;
    String logUrl;

    public void generateLogUrl(String krameriusUrl) {
        this.logUrl = krameriusUrl + "/search/inc/admin/_processes_outputs.jsp?uuid=" + this.uuid;
        if (this.children != null && !this.children.isEmpty()) {
            this.children.forEach(ch -> ch.generateLogUrl(krameriusUrl));
        }
    }
}
