package cz.mzk.fofola.rest.request.dnnt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SugoTransitionRequestFilter {
    private List<Long> ids;
    private List<Long> sessionIds;
    private String uuid;
    private String from;
    private String to;
    private Integer page;
}
