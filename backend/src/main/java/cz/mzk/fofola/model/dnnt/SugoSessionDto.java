package cz.mzk.fofola.model.dnnt;

import cz.mzk.fofola.constants.dnnt.Direction;
import cz.mzk.fofola.constants.dnnt.Operation;
import cz.mzk.fofola.constants.dnnt.Requestor;
import cz.mzk.fofola.constants.dnnt.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SugoSessionDto {
    private Long id;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private Direction direction;
    private Requestor requestor;
    private Operation operation;
    private Status status;
}
