package cz.mzk.fofola.enums.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DnntLabelEnum {

    DNNTO("dnnto"),
    DNNTT("dnntt"),
    COVID("covid");

    private final String value;

    DnntLabelEnum(final String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @JsonCreator
    public static DnntLabelEnum of(final String value) {
        if (value == null) {
            return null;
        }

        for (final DnntLabelEnum label : DnntLabelEnum.values()) {
            if (label.value.equals(value)) {
                return label;
            }
        }
        return null;
    }
}
