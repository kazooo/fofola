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
public class SugoJobDto {
    private String id;
    private String title;
    private String description;

    private String cronExpression;
    private String cronExpressionExplanation;
    private Boolean active;

    private SugoJobOperationDto operation;

    private LocalDateTime created;
    private LocalDateTime lastExecution;
    private LocalDateTime nextExecution;
}
