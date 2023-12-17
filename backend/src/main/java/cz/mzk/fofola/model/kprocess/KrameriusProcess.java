package cz.mzk.fofola.model.kprocess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class KrameriusProcess {

    @JsonProperty("id")
    private String id;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("state")
    private String state;

    @JsonProperty("defid")
    private String defId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("planned")
    private String planned;

    @JsonProperty("started")
    private String started;

    @JsonProperty("finished")
    private String finished;
}
