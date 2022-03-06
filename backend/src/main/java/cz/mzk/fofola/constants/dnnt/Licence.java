package cz.mzk.fofola.constants.dnnt;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Licence {
    A("A"),
    PA("PA"),
    N("N"),
    X("X"),
    NZ("NZ"),
    Q("Q"),
    A_NZ("A-NZ"),
    PA_NZ("PA-NZ");

    @Getter
    @JsonValue
    private final String value;

    public static Licence of(final String value) {
        for (final Licence licence : Licence.values()) {
            if (licence.getValue().equals(value)) {
                return licence;
            }
        }
        throw new IllegalArgumentException("Unknown licence \"" + value + "\"!");
    }
}
