package cz.mzk.fofola.model.kprocess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class KrameriusBatchResponse {

    @JsonProperty("batches")
    private List<KrameriusAggregatedBatch> batches;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("total_size")
    private int totalSize;

    @Data
    @EqualsAndHashCode
    public static class KrameriusAggregatedBatch {

        @JsonProperty("processes")
        private List<KrameriusProcess> processes;

        @JsonProperty("batch")
        private KrameriusBatch batch;
    }
}
