package cz.mzk.fofola.model.dnnt.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.mzk.fofola.constants.dnnt.Direction;
import cz.mzk.fofola.constants.dnnt.Operation;
import cz.mzk.fofola.constants.dnnt.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoJobOperationDto {
    private JobType type;
    private Operation operation;
    private Direction direction;
    @Builder.Default
    private Boolean recursive = true;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime from;
    @Builder.Default
    private Boolean fromRelative = false;
    private Long relativeFrom;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime to;

    private String solrQuery;
    private String solrFilterQuery;
}
