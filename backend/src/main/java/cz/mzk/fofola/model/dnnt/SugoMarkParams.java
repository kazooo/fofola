package cz.mzk.fofola.model.dnnt;

import cz.mzk.fofola.constants.dnnt.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SugoMarkParams {
    private Label label;
    private Boolean recursively;
}
