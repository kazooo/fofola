package cz.mzk.fofola.model.dnnt;

import cz.mzk.fofola.constants.dnnt.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SugoTransitionDto {
    private Long id;
    private LocalDateTime changeDateTime;

    private String uuid;
    private String name;
    private String model;
    private String access;
    private String cnb;

    private String sourceIdentifier;
    private String sourceUuid;

    private List<Label> currentLabels;
    private Boolean currentDnntFlag;
    private List<Label> newLabels;
    private Boolean newDnntFlag;

    private String notes;
}
