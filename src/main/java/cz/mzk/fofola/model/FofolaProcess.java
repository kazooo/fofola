package cz.mzk.fofola.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "fofola_process")
public class FofolaProcess implements Serializable {

    public static final String CHECK_SOLR_TYPE = "check_solr";
    public static final String CHECK_FEDORA_TYPE = "check_fedora";
    public static final String GENERATE_SITEMAP_TYPE = "generate_sitemap";

    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_STOPPED = "stopped";
    public static final String STATUS_FINISHED = "finished";

    private static final long serialVersionUID = -2343243243242432341L;

    public FofolaProcess(String type) {
        processType = type;
        status = STATUS_RUNNING;
    }

    public FofolaProcess() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long processId;

    @NotNull
    @Column(name = "type")
    private String processType;

    @NotNull
    @Column(name = "status")
    private String status;

    public long getId() {
        return processId;
    }

    public boolean isRunning() { return status.equals(STATUS_RUNNING); }

    public void stop() { status = STATUS_STOPPED; }

    public void finish() { status = STATUS_FINISHED; }
}
