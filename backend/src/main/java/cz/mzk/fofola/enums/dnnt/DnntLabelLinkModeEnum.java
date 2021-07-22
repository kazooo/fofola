package cz.mzk.fofola.enums.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DnntLabelLinkModeEnum {

    LINK("link"),
    UNLINK("unlink");

    private final String value;

    private DnntLabelLinkModeEnum(String value) {
        this.value = value;
    }

    @JsonCreator
    public static DnntLabelLinkModeEnum of(String value) {
        for (DnntLabelLinkModeEnum linkMode : DnntLabelLinkModeEnum.values()) {
            if (linkMode.value.equals(value)) {
                return linkMode;
            }
        }
        return null;
    }
}
