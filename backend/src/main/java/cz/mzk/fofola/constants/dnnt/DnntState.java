package cz.mzk.fofola.constants.dnnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DnntState {
    private List<String> labels;
    private Boolean dnntFlag;
}
