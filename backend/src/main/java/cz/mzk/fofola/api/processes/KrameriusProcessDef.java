package cz.mzk.fofola.api.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@Builder
public class KrameriusProcessDef {

    @JsonProperty("defid")
    private String defId;

    @JsonProperty("params")
    private Map<String, Object> params;
}
