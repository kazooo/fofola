package cz.mzk.fofola.rest.request.dnnt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SugoAlertFilter {
    private Boolean solved;
    private Integer page;
    private Integer size;
    private Integer offset;
    private String sort;
}
