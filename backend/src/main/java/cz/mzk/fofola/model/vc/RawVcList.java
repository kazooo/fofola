package cz.mzk.fofola.model.vc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawVcList {

    @JsonProperty("total_size")
    private int totalSize;

    @JsonProperty("collections")
    private List<RawVc> collections;
}
