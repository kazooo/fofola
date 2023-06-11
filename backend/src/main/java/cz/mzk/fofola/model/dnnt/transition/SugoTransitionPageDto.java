package cz.mzk.fofola.model.dnnt.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoTransitionPageDto {
    private Long numFound;
    private List<SugoTransitionDto> entities;
}
