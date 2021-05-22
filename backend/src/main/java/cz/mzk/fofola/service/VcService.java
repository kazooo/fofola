package cz.mzk.fofola.service;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.model.vc.VirtualCollection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class VcService {

    private final FedoraApi fedoraApi;

    public String createVc(VirtualCollection virtualCollection) {
        return "";
    }

    public String updateVc(VirtualCollection virtualCollection) throws IOException {
        String uuid = virtualCollection.getUuid();
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
