package cz.mzk.fofola.processes.img_edition;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.models.Process;
import cz.mzk.fofola.processes.utils.FedoraClient;
import cz.mzk.fofola.processes.utils.FedoraUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;

public class ImgEditingProcess extends Process {

    private final MultipartFile image;
    private final String dsType;
    private final String pageUuid;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;

    public ImgEditingProcess(LinkedHashMap<String, Object> params,
                             FofolaConfiguration fofolaConfiguration) throws IOException {
        super(params);
        image = (MultipartFile) params.get("image");
        dsType = (String) params.get("img_datastream");
        pageUuid = (String) params.get("page_uuid");
        fedoraHost = fofolaConfiguration.getFedoraHost();
        fedoraUser = fofolaConfiguration.getFedoraUser();
        fedoraPswd = fofolaConfiguration.getFedoraPswd();
    }

    @Override
    public void process() throws Exception {
        FedoraClient fedoraClient = new FedoraClient(fedoraHost, fedoraUser, fedoraPswd);
        FedoraUtils.DATASTREAMS datastream = FedoraUtils.DATASTREAMS.getDataStream(dsType);
        if (datastream == null)
            throw new IllegalStateException("Can't determine data stream type for name: " + dsType);
        if (datastream == FedoraUtils.DATASTREAMS.THUMB_IMG) {
            fedoraClient.setThumbnailImg(pageUuid, image);
        } else if (datastream == FedoraUtils.DATASTREAMS.FULL_IMG) {
            fedoraClient.setFullImg(pageUuid, image);
        } else {
            throw new IllegalStateException("Data stream " + dsType + " is not an image data stream!");
        }
    }
}
