package cz.mzk.integrity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "uuid_problem")
public class UuidProblem {

    public static final String NOT_STORED = "not_stored";
    public static final String DIFF_VISIBILITY = "diff_visibility";
    public static final String ROOT_NOT_STORED = "root_not_stored";
    public static final String ROOT_NOT_INDEXED = "root_not_indexed";
    public static final String NO_ROOT = "no_root";

    private static final Map<String, String> problemShortDescs =
            new HashMap<String, String>() {
        {
            put(NOT_STORED, "Není uloženo ve Fedoře");
            put(DIFF_VISIBILITY, "Různá viditelnost ve Fedoře a v Solru");
            put(ROOT_NOT_STORED, "Kořenový dokument není ve Fedoře");
            put(ROOT_NOT_INDEXED, "Kořenový dokument není v Solru");
            put(NO_ROOT, "Nemá kořenový dokument");
        }
    };

    public UuidProblem() { }

    public UuidProblem(String type) {
        this.type = type;
        this.shortDesc = problemShortDescs.get(type);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long problemId;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "short_desc")
    private String shortDesc;

    public String getType() { return type; }

    public String getShortDesc() {
        return shortDesc;
    }
}
