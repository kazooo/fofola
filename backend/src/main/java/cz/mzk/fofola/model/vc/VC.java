package cz.mzk.fofola.model.vc;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class VC {
    public String pid;
    public Descs descs;
    public LongDescs longDescs;
}
