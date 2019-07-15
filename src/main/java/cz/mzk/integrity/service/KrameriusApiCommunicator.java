package cz.mzk.integrity.service;

import cz.mzk.integrity.kramerius_api.KrameriusProcessRemoteApiFactory;
import cz.mzk.integrity.kramerius_api.ProcessRemoteApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class KrameriusApiCommunicator {

    private ProcessRemoteApi remoteApi;

    public KrameriusApiCommunicator(@Value("${spring.data.kramerius.host}") String krameriusUrl,
                                    @Value("${spring.data.kramerius.user}") String krameriusUser,
                                    @Value("${spring.data.kramerius.pswd}") String krameriusPswd) {
        this.remoteApi = KrameriusProcessRemoteApiFactory.getProcessRemoteApi(krameriusUrl, krameriusUser, krameriusPswd);
    }

    public void makePublic(String uuid) throws Exception {
        remoteApi.setPublic(uuid);
    }

    public void makePrivate(String uuid) throws Exception {
        remoteApi.setPrivate(uuid);
    }
}
