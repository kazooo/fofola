package cz.mzk.fofola.model.dnnt;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private String id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime modifiedDate;

    private String uuid;
    private String name;
    private String model;
    private String access;
    private String cnb;

    private List<String> previousLabels;
    private List<String> actualLabels;

    private String notes;
}
