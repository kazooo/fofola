package cz.mzk.fofola.constants.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarkMode {

    LINK("link", Operation.ADD_LABEL),
    UNLINK("unlink", Operation.REMOVE_LABEL),
    SYNC("sync", Operation.SYNCHRONIZE),
    CLEAN("clean", Operation.CLEAN_LABELS);

    private final String value;
    private final Operation operation;

    @JsonCreator
    public static MarkMode of(String value) {
        for (MarkMode linkMode : MarkMode.values()) {
            if (linkMode.value.equals(value)) {
                return linkMode;
            }
        }
        return null;
    }
}
