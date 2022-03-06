package cz.mzk.fofola.model.dnnt;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SugoDataDto {
    private String uuid;
    private String name;
    private String model;
    private String access;
    private String cnb;
    private List<String> labels;

    private String sourceIdentifier;
    private String sourceUuid;
    private String sourceLicence;
}
