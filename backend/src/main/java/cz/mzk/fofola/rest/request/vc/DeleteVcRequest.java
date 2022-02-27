package cz.mzk.fofola.rest.request.vc;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteVcRequest {
    private String uuid;
    private String nameCz;
    private String nameEn;
}
