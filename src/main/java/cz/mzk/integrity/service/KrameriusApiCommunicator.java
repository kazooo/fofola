package cz.mzk.integrity.service;

import cz.mzk.integrity.kramerius_api.KrameriusProcessRemoteApiFactory;
import cz.mzk.integrity.kramerius_api.ProcessRemoteApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cz.mzk.integrity.kramerius_api.Process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KrameriusApiCommunicator {

    private ProcessRemoteApi remoteApi;
    private final String krameriusUrl;

    public KrameriusApiCommunicator(@Value("${spring.data.kramerius.host}") String krameriusUrl,
                                    @Value("${spring.data.kramerius.user}") String krameriusUser,
                                    @Value("${spring.data.kramerius.pswd}") String krameriusPswd) {
        this.remoteApi = KrameriusProcessRemoteApiFactory.getProcessRemoteApi(krameriusUrl, krameriusUser, krameriusPswd);
        this.krameriusUrl = krameriusUrl;
    }

    public void makePublic(String uuid) throws Exception {
        remoteApi.setPublic(uuid);
    }

    public void makePrivate(String uuid) throws Exception {
        remoteApi.setPrivate(uuid);
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

    public void stopProcess(String pid) throws Exception {
        remoteApi.stopProcess(pid);
    }

    public void removeProcess(String pid) throws Exception {
        remoteApi.removeProcess(pid);
    }
}
