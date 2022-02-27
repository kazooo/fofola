package cz.mzk.fofola.model.dnnt;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.mzk.fofola.constants.dnnt.Label;
import cz.mzk.fofola.model.dnnt.serializer.LocalDateTimeSerializer;
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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
