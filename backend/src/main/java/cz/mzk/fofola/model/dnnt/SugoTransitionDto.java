package cz.mzk.fofola.model.dnnt;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.mzk.fofola.constants.dnnt.DnntState;
import cz.mzk.fofola.model.dnnt.serializer.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SugoTransitionDto {
    private String id;
    private String sessionId;
    private String uuid;
    private String type;
    private String parentUuid;
    private DnntState previousState;
    private DnntState currentState;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;
}
