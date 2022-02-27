package cz.mzk.fofola.constants.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarkMode {

    LINK("link"),
    UNLINK("unlink"),
    SYNC("sync"),
    CLEAN("clean");

    private final String value;

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
