package cz.mzk.fofola.model.dnnt;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.mzk.fofola.constants.dnnt.Direction;
import cz.mzk.fofola.constants.dnnt.Operation;
import cz.mzk.fofola.constants.dnnt.Requestor;
import cz.mzk.fofola.constants.dnnt.Status;
import cz.mzk.fofola.model.dnnt.serializer.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SugoSessionDto {
    private String id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime finished;
    private Direction direction;
    private Requestor requestor;
    private Operation operation;
    private Long total;
    private Long done;
    private Status status;
}
