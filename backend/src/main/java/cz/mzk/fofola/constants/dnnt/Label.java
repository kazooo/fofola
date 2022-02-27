package cz.mzk.fofola.constants.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Label {

    DNNTO("dnnto"),
    DNNTT("dnntt"),
    COVID("covid");

    private final String value;

    @JsonCreator
    public static Label of(final String value) {
        if (value == null) {
            return null;
        }

        for (final Label label : Label.values()) {
            if (label.value.equals(value)) {
                return label;
            }
        }
        return null;
    }
}
