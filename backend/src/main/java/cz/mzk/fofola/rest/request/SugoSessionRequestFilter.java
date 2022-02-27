package cz.mzk.fofola.rest.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.mzk.fofola.constants.dnnt.Direction;
import cz.mzk.fofola.constants.dnnt.Operation;
import cz.mzk.fofola.constants.dnnt.Requestor;
import cz.mzk.fofola.constants.dnnt.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SugoSessionRequestFilter {
    private String from;
    private String to;
    private Direction direction;
    private Requestor requestor;
    private Operation operation;
    private Status status;
    private Integer page;
    private Integer size;
    private Integer offset;
    private String sort;
}
