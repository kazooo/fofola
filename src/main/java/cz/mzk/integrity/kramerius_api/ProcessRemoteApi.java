package cz.mzk.integrity.kramerius_api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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