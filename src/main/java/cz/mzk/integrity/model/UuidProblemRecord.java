package cz.mzk.integrity.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "uuid_problem_record")
public class UuidProblemRecord implements Serializable {

    public static final Map<String, String> fileNameByType = new HashMap<String, String>() {{
        put(UuidProblem.NOT_STORED, "not_stored.txt");
        put(UuidProblem.ROOT_NOT_STORED, "root_not_stored.txt");
        put(UuidProblem.ROOT_NOT_INDEXED, "root_not_indexed.txt");
        put(UuidProblem.DIFF_VISIBILITY, "diff_visibility.txt");
        put(UuidProblem.NO_ROOT, "no_root.txt");
        put(UuidProblem.NO_MODEL, "no_model.txt");
        put(UuidProblem.NO_ACCESS, "no_access.txt");
        put(UuidProblem.NO_IMAGE, "no_images.txt");
        put(UuidProblem.NO_PARENT, "no_parent.txt");
        put(UuidProblem.PARENT_NOT_INDEXED, "parent_not_indexed.txt");
        put(UuidProblem.PARENT_NOT_STORED, "parent_not_stored.txt");
        put(UuidProblem.NO_CHILD, "no_child.txt");
        put(UuidProblem.CHILD_NOT_INDEXED, "child_not_indexed.txt");
        put(UuidProblem.CHILD_NOT_STORED, "child_not_stored.txt");
    }};

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
