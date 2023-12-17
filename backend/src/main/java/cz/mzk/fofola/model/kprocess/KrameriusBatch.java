package cz.mzk.fofola.model.kprocess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class KrameriusBatch {

    @JsonProperty("id")
    private String id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("token")
    private String token;

    @JsonProperty("started")
    private String started;

    @JsonProperty("finished")
    private String finished;

    @JsonProperty("planned")
    private String planned;

    @JsonProperty("owner_id")
    private String ownerId;

    @JsonProperty("owner_name")
    private String ownerName;
}