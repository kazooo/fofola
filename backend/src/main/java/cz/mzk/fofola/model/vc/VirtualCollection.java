package cz.mzk.fofola.model.vc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualCollection {
    private String uuid;
    private String nameCz;
    private String nameEn;
    private String descriptionCz;
    private String descriptionEn;
    private MultipartFile fullImg;
    private MultipartFile thumbImg;

    public static VirtualCollection from(final VC vc) {
        final VirtualCollection virtualCollection = new VirtualCollection();
        virtualCollection.setUuid(vc.getPid());

        if (vc.getDescs() != null) {
            virtualCollection.setNameCz(vc.getDescs().getCs());
            virtualCollection.setNameEn(vc.getDescs().getEn());
        }

        if (vc.getLongDescs() != null) {
            virtualCollection.setDescriptionCz(vc.getLongDescs().getCs());
            virtualCollection.setDescriptionEn(vc.getLongDescs().getEn());
        }

        return virtualCollection;
    }
}
