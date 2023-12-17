package cz.mzk.fofola.service;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.vc.VirtualCollection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VcService {

    private final KrameriusApi krameriusApi;

    public List<VirtualCollection> getAllVcs() {
        return krameriusApi.getAllVcs();
    }

    public String createVc(final VirtualCollection vc) {
        return krameriusApi.createVc(vc).getUuid();
    }

    public String updateVc(final VirtualCollection vc) {
        krameriusApi.updateVc(vc);
        return vc.getUuid();
    }

    public String deleteVc(final String uuid) {
        krameriusApi.deleteVc(uuid);
        return uuid;
    }
}
