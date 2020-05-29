package cz.mzk.fofola.service;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.kramerius_api.KrameriusProcessRemoteApiFactory;
import cz.mzk.fofola.kramerius_api.ProcessRemoteApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cz.mzk.fofola.kramerius_api.Process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KrameriusApiCommunicator {

    private ProcessRemoteApi remoteApi;
    private final String krameriusUrl;

    public KrameriusApiCommunicator(FofolaConfiguration fofolaConfig) {
        this.remoteApi = KrameriusProcessRemoteApiFactory.getProcessRemoteApi(
                fofolaConfig.getKrameriusHost(),
                fofolaConfig.getKrameriusUser(),
                fofolaConfig.getKrameriusPswd()
        );
        this.krameriusUrl = fofolaConfig.getKrameriusHost();
    }

    public Process makePublic(String uuid) throws Exception {
        return remoteApi.setPublic(uuid);
    }

    public Process makePrivate(String uuid) throws Exception {
        return remoteApi.setPrivate(uuid);
    }

    public void reindex(String uuid) throws Exception {
        remoteApi.reindex(uuid);
    }

    public List<Process> getProcessList(int offset, int processes) throws Exception {
        Map<String, String> filterFields = new HashMap<String, String>() {{
            put("ordering", "DESC");
            put("resultSize", Integer.toString(processes));
            put("offset", Integer.toString(offset));
        }};
        List<Process> processList = remoteApi.listProcesses(filterFields);
        processList.forEach(p -> p.generateLogUrl(krameriusUrl));
        return processList;
    }

    public Process getProcessInfo(String processUuid) throws Exception {
        Process p = remoteApi.getProcessInfo(processUuid);
        p.generateLogUrl(krameriusUrl);
        return p;
    }

    public void stopProcess(String pid) throws Exception {
        remoteApi.stopProcess(pid);
    }

    public void removeProcess(String pid) throws Exception {
        remoteApi.removeProcess(pid);
    }
}
