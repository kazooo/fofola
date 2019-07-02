package cz.mzk.integrity.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "uuid_problem")
public class UuidProblem implements Serializable {

    public static final String NOT_STORED = "not_stored";

    public UuidProblem() { }

    public UuidProblem(long processId, String uuid, String type) {
        this.processId = processId;
        this.uuid = uuid;
        this.problemType = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long problemId;

    @NotNull
    private long processId;

    @NotNull
    @Column(name = "uuid")
    private String uuid;

    @NotNull
    @Column(name = "type")
    private String problemType;

    public String getUuid() {
        return uuid;
    }

    public String getProblemType() {
        return problemType;
    }
}
