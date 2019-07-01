package cz.mzk.integrity.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "uuid_problem")
public class UuidProblem {

    private static final String NOT_STORED = "not_stored";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long problemId;

    @Id
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
