package cz.mzk.fofola.kramerius_api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProcessRemoteApi {

    private ProcessRemoteApiRetrofit api;

    ProcessRemoteApi(ProcessRemoteApiRetrofit api) {
        this.api = api;
    }

    public Process setPublic(String pid_path) throws Exception {
        return api.planProcess("setpublic", new Parameters(pid_path, pid_path));
    }

    public Process setPrivate(String pid_path) throws Exception {
        return api.planProcess("setprivate", new Parameters(pid_path, pid_path));
    }

    public Process reindex(String pid_path) throws Exception {
        return api.planProcess("reindex", new Parameters("fromKrameriusModelNoCheck", pid_path, pid_path));
    }

    public List<Process> listProcesses(Map<String, String> fields) throws Exception {
        return api.filterProcesses(fields);
    }

    public Process getProcessInfo(String processUuid) throws Exception {
        return api.getProcess(processUuid);
    }

    public Process stopProcess(String processUuid) throws Exception {
        return api.stopProcess(processUuid, new Parameters(""));
    }

    public void removeProcess(String processUuid) throws Exception {
        api.deleteProcessLog(processUuid);
    }
}

class Parameters {
    // {"parameters":["first","second","third"]}
    // "{"parameters":[" + pid_path + "," + pid_path + "]}";
    private final List<String> parameters;

    Parameters(String... parameters) {
        this.parameters = new ArrayList<String>();
        this.parameters.addAll(Arrays.asList(parameters));
    }
}