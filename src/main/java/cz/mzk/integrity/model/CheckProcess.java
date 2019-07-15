package cz.mzk.integrity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "fofola_check_process")
public class CheckProcess implements Serializable {

    public static final String CHECK_SOLR_TYPE = "check_solr";
    public static final String CHECK_FEDORA_TYPE = "check_fedora";

    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_STOPPED = "stopped";

    private static final long serialVersionUID = -2343243243242432341L;

    public CheckProcess(String type, String model, long docs) {
        processType = type;
        this.model = model;
        docCount = docs;
        status = STATUS_RUNNING;
    }

    public CheckProcess() { }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long processId;

    @NotNull
    @Column(name = "type")
    private String processType;

    @NotNull
    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "docs")
    private long docCount;

    @NotNull
    @Column(name = "status")
    private String status;

    public long getId() {
        return processId;
    }

    public boolean isRunning() { return status.equals(STATUS_RUNNING); }

    public void stop() { status = STATUS_STOPPED; }
}
