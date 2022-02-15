package cz.mzk.fofola.enums.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DnntLabel {

    DNNTO("dnnto"),
    DNNTT("dnntt"),
    COVID("covid");

    private final String value;

    @JsonCreator
    public static DnntLabel of(final String value) {
        if (value == null) {
            return null;
        }

        for (final DnntLabel label : DnntLabel.values()) {
            if (label.value.equals(value)) {
                return label;
            }
        }
        return null;
    }
}
