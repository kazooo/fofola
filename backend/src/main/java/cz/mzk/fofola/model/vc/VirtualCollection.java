package cz.mzk.fofola.model.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualCollection {
    private String uuid;
    private String nameCz;
    private String nameEn;
    private String descriptionCz;
    private String descriptionEn;
    private MultipartFile fullImg;
    private MultipartFile thumbImg;
}
