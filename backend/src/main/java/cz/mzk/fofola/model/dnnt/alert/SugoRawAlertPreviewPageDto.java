package cz.mzk.fofola.model.dnnt.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SugoRawAlertPreviewPageDto {
    private Long numFound;
    private List<SugoRawAlertPreviewDto> entities;
}
