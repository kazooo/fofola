package cz.mzk.fofola.service;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.KrameriusProcess;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class KProcessService {

    private final KrameriusApi krameriusApi;

    public KrameriusProcess makePublic(String uuid) {
        return krameriusApi.planNewProcess("setpublic", uuid, uuid);
    }

    public KrameriusProcess makePrivate(String uuid) {
        return krameriusApi.planNewProcess("setprivate", uuid, uuid);
    }

    public KrameriusProcess reindex(String uuid) {
        return krameriusApi.planNewProcess("reindex", "fromKrameriusModelNoCheck", uuid, uuid);
    }

    public KrameriusProcess delete(String uuid) {
        return krameriusApi.planNewProcess("delete", uuid, uuid);
    }

    public KrameriusProcess deleteFromIndex(final String uuid, final boolean recursively) {
        if (recursively) {
            return krameriusApi.planNewProcess("reindex", "deleteDocument", uuid, uuid);
        } else {
            return krameriusApi.planNewProcess("reindex", "deletePid", uuid, uuid);
        }
    }
}
