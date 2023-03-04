package cz.mzk.fofola.model.dnnt.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CreateSugoJobDto {
    private String title;
    private String description;

    private String cronExpression;
    private String cronExpressionExplanation;
    private Boolean active;

    private SugoJobOperationDto operation;
}
