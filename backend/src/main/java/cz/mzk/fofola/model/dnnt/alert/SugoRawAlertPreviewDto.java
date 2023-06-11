package cz.mzk.fofola.model.dnnt.alert;

import cz.mzk.fofola.constants.dnnt.AlertIssueType;
import cz.mzk.fofola.constants.dnnt.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SugoRawAlertPreviewDto {
    private String id;
    private AlertType type;
    private AlertIssueType issueType;
    private LocalDateTime created;
    private Map<String, Object> parameters;
    private Boolean solved;
}
