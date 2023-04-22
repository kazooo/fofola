package cz.mzk.fofola.constants.dnnt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BasicLabel implements Label {
    DNNTO("DNNTO"),
    DNNTT("DNNTT"),
    COVID("COVID"),
    LICENSE("LICENSE"),

    NO_LABELS("NO_LABELS");

    private final String value;

    public static Label of(final String value) {
        if (value == null) {
            return null;
        }
        for (final Label label : BasicLabel.values()) {
            if (label.getValue().equals(value.toLowerCase())) {
                return label;
            }
        }
        return new DynamicLabel(value);
    }
}
