package cz.mzk.fofola.enums.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DnntLabelLinkMode {

    LINK("link"),
    UNLINK("unlink"),
    SYNC("sync"),
    CLEAN("clean");

    private final String value;

    @JsonCreator
    public static DnntLabelLinkMode of(String value) {
        for (DnntLabelLinkMode linkMode : DnntLabelLinkMode.values()) {
            if (linkMode.value.equals(value)) {
                return linkMode;
            }
        }
        return null;
    }
}
