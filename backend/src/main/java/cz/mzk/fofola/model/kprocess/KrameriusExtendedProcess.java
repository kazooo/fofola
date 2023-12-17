package cz.mzk.fofola.model.kprocess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class KrameriusExtendedProcess {

    @JsonProperty("process")
    private KrameriusProcess process;

    @JsonProperty("batch")
    private KrameriusBatch batch;
}
