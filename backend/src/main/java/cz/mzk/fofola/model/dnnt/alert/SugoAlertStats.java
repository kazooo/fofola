package cz.mzk.fofola.model.dnnt.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoAlertStats {
    private Long warnings;
    private Long errors;
}
