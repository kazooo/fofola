package cz.mzk.fofola.model.dnnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoSessionPageDto {
    private Long numFound;
    private List<SugoSessionDto> sessions;
}
