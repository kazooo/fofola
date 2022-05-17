package cz.mzk.fofola.model.vc;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class Descs {
    public String cs;
    public String en;
}
