package cz.mzk.fofola.model.pdf;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PDFGenState {
    
    ACTIVE("active"),

    WAITING("waiting"),
    FINISHED("finished"),
    EXCEPTION("exception");

    private final String state;
}
