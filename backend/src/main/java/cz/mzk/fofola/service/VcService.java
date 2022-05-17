package cz.mzk.fofola.service;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.model.vc.VirtualCollection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class VcService {

    private final KrameriusApi krameriusApi;
    private final FedoraApi fedoraApi;

    public String createVc(final VirtualCollection virtualCollection) throws IOException {
        final VC newVirtualCollection = krameriusApi.createEmptyVc();
        virtualCollection.setUuid(newVirtualCollection.getPid());
        return updateVc(virtualCollection);
    }

    public String updateVc(final VirtualCollection virtualCollection) throws IOException {
        final String uuid = virtualCollection.getUuid();

        fedoraApi.setTextCz(uuid, virtualCollection.getNameCz());
        fedoraApi.setTextEn(uuid, virtualCollection.getNameEn());
        fedoraApi.setLongTextCz(uuid, virtualCollection.getDescriptionCz());
        fedoraApi.setLongTextEn(uuid, virtualCollection.getDescriptionEn());

        if (virtualCollection.getFullImg() != null) {
            fedoraApi.setFullImg(uuid, virtualCollection.getFullImg());
        }
        if (virtualCollection.getThumbImg() != null) {
            fedoraApi.setThumbnailImg(uuid, virtualCollection.getThumbImg());
        }
        return uuid;
    }

    public String deleteVc(final String uuid) {
        return krameriusApi.deleteVc(uuid);
    }
}
