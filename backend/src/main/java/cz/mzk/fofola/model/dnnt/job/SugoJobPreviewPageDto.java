package cz.mzk.fofola.model.dnnt.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoJobPreviewPageDto {
    private Long numFound;
    private List<SugoJobPreviewDto> entities;
}
