package cz.mzk.integrity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "fofola_process")
public class Process implements Serializable {

    public static final String CHECK_SOLR_TYPE = "check_solr";
    public static final String CHECK_FEDORA_TYPE = "check_fedora";

    private static final long serialVersionUID = -2343243243242432341L;

    public Process(String type, String model, long docs) {
        processType = type;
        this.model = model;
        docCount = docs;
    }

    public Process() { }


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

    public long getId() {
        return processId;
    }
}
