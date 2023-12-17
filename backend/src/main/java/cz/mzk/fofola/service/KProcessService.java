package cz.mzk.fofola.service;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.api.processes.KrameriusIndexationType;
import cz.mzk.fofola.model.kprocess.KrameriusProcess;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KProcessService {

    private final KrameriusApi krameriusApi;

    public KrameriusProcess makePublic(final String uuid) {
        return krameriusApi.makePublic(uuid, KrameriusIndexationType.TREE_AND_FOSTER_TREES);
    }

    public KrameriusProcess makePrivate(final String uuid) {
        return krameriusApi.makePrivate(uuid, KrameriusIndexationType.TREE_AND_FOSTER_TREES);
    }

    public KrameriusProcess reindex(final String uuid) {
        return krameriusApi.reindexDoc(uuid);
    }

    public KrameriusProcess delete(final String uuid) {
        return krameriusApi.deleteDoc(uuid);
    }
}
