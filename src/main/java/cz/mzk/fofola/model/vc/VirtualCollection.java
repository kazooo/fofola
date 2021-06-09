package cz.mzk.fofola.model.vc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualCollection {
    private String uuid;
    private String nameCz;
    private String nameEn;
    private String descriptionCz;
    private String descriptionEn;
}
