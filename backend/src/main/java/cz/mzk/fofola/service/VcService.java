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
        final VC newVirtualCollection = krameriusApi.createEmptyVirtualCollection();
        virtualCollection.setUuid(newVirtualCollection.getPid());
        return updateVc(virtualCollection);
    }

    public String updateVc(final VirtualCollection virtualCollection) throws IOException {
        final String uuid = virtualCollection.getUuid();
        if (virtualCollection.getNameCz() != null) {
            fedoraApi.setTextCz(uuid, virtualCollection.getNameCz());
        }
        if (virtualCollection.getNameEn() != null) {
            fedoraApi.setTextEn(uuid, virtualCollection.getNameEn());
        }
        if (virtualCollection.getDescriptionCz() != null) {
            fedoraApi.setLongTextCz(uuid, virtualCollection.getDescriptionCz());
        }
        if (virtualCollection.getDescriptionEn() != null) {
            fedoraApi.setLongTextEn(uuid, virtualCollection.getDescriptionEn());
        }
        return uuid;
    }
}
