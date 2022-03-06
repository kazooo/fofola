package cz.mzk.fofola.constants.dnnt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DynamicLabel implements Label {
    private final String value;
}
