package cz.mzk.fofola.enums.dnnt;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DnntLabelEnum {

    DNNTO("dnnto"),
    DNNTT("dnntt"),
    COVID("covid");

    private String value;

    private DnntLabelEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @JsonCreator
    public static DnntLabelEnum of(String value) {
        for (DnntLabelEnum label : DnntLabelEnum.values()) {
            if (label.value.equals(value)) {
                return label;
            }
        }
        return null;
    }
}
