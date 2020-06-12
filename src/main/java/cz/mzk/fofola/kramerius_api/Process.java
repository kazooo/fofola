package cz.mzk.fofola.kramerius_api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by holmanj on 16.11.15.
 */
@Getter
@Setter
@ToString(exclude = {"children"})
@EqualsAndHashCode
public class Process {

    String uuid; // bez předpony "uuid:"
    String pid; // interní pid krameria
    String def; // typ procesu
    String state;
    String batchState;
    String name;
    String started;
    String planned;
    String finished;
    String userid;
    String userFirstname;
    String userSurname;
    List<Process> children;
    String logUrl;

    public void generateLogUrl(String krameriusUrl) {
        this.logUrl = "http://" + krameriusUrl + "/search/inc/admin/_processes_outputs.jsp?uuid=" + this.uuid;
        if (this.children != null && !this.children.isEmpty()) {
            this.children.forEach(ch -> ch.generateLogUrl(krameriusUrl));
        }
    }
}
