package cz.mzk.fofola.model.dnnt.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SugoAlertPreviewPageDto {
    private Long numFound;
    private List<SugoAlertPreviewDto> entities;
}
