package cz.mzk.fofola.constants.dnnt;

import com.fasterxml.jackson.annotation.JsonValue;

public interface Label {
    @JsonValue
    String getValue();
    boolean equals(Object obj);
    int hashCode();
}
