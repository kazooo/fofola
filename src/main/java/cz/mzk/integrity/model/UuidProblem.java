package cz.mzk.integrity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "uuid_problem")
public class UuidProblem {

    public static final String NO_MODEL = "no model";
    public static final String NO_ACCESS = "no accessibility";
    public static final String NO_IMAGE = "no image";

    public static final String NOT_STORED = "not_stored";
    public static final String DIFF_VISIBILITY = "diff_visibility";

    public static final String NO_PARENT = "no_parent";
    public static final String PARENT_NOT_INDEXED = "parent_not_indexed";
    public static final String PARENT_NOT_STORED = "parent_not_stored";

    public static final String NO_ROOT = "no_root";
    public static final String ROOT_NOT_INDEXED = "root_not_indexed";
    public static final String ROOT_NOT_STORED = "root_not_stored";

    public static final String NO_CHILD = "no_child";
    public static final String CHILD_NOT_INDEXED = "child_not_indexed";
    public static final String CHILD_NOT_STORED = "child_not_stored";

    private static final Map<String, String> problemShortDescs =
            new HashMap<String, String>() {
        {
            put(NO_MODEL, "Nemá model v DC datastreamu");

            put(NOT_STORED, "Není uloženo ve Fedoře");
            put(DIFF_VISIBILITY, "Různá viditelnost ve Fedoře a v Solru");

            put(NO_ROOT, "Nemá kořenový dokument");
            put(ROOT_NOT_INDEXED, "Kořenový dokument není v Solru");
            put(ROOT_NOT_STORED, "Kořenový dokument není ve Fedoře");

            put(NO_PARENT, "Nemá nadřizený dokument");
            put(PARENT_NOT_INDEXED, "Nadřizený dokument není v Solru");
            put(PARENT_NOT_STORED, "Nadřizený dokument není ve Fedoře");

            put(NO_CHILD, "Nemá podřizené dokumenty");
            put(CHILD_NOT_INDEXED, "Podřizené dokumenty nejsou v Solru");
            put(CHILD_NOT_STORED, "Podřizené dokumenty nejsou ve Fedoře");
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
