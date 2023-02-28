package cz.mzk.fofola.model.dnnt.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoJobPreviewDto {
    private String id;
    private String title;
    private String description;
    private String cronExpressionExplanation;
    private Boolean active;
    private LocalDateTime lastExecution;
    private LocalDateTime nextExecution;
}
