package cz.mzk.integrity.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "uuid_problem_record")
public class UuidProblemRecord implements Serializable {

    public static final String NOT_STORED_FILE_NAME = "not_stored_in_fedora.txt";

    public UuidProblemRecord() { }

    public UuidProblemRecord(long processId, String uuid, String title,
                             String model, List<UuidProblem> problems) {
        this.processId = processId;
        this.uuid = uuid;
        this.model = model;
        this.title = title;
        this.problems = problems;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long problemRecordId;

    @NotNull
    private long processId;

    @NotNull
    @Column(name = "uuid")
    private String uuid;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UuidProblem> problems;

    @NotNull
    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "title")
    private String title;

    public String getUuid() {
        return uuid;
    }

    public List<UuidProblem> getProblems() {
        return problems;
    }

    public String getTitle() {
        return title;
    }

    public String getModel() {
        return model;
    }
}
