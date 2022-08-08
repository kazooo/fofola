package cz.mzk.fofola.model.dnnt;

import cz.mzk.fofola.constants.dnnt.Direction;
import cz.mzk.fofola.constants.dnnt.Label;
import cz.mzk.fofola.constants.dnnt.Operation;
import cz.mzk.fofola.constants.dnnt.Requestor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SugoMarkParams {
    private Label label;
    private Boolean recursively;
    private Direction direction;
    private Requestor requestor;
    private Operation operation;
    private List<String> uuids;
}
