package cz.mzk.fofola.rest.request.dnnt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SugoDataRequestFilter {
    private List<String> internalUuids;
    private String cnb;
    private String sourceUuid;
}
