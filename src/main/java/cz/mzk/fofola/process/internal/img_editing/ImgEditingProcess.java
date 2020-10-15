package cz.mzk.fofola.process.internal.img_editing;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.process.management.ProcessParams;
import cz.mzk.fofola.process.constants.TerminationReason;
import cz.mzk.fofola.process.utils.FedoraClient;
import cz.mzk.fofola.process.utils.FedoraUtils;
import org.springframework.web.multipart.MultipartFile;
import cz.mzk.fofola.process.management.Process;

import java.io.IOException;
import java.util.Map;


public class ImgEditingProcess extends Process {

    private final MultipartFile image;
    private final String dsType;
    private final String pageUuid;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;

    public ImgEditingProcess(ProcessParams params) throws IOException {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();

        image = (MultipartFile) data.get("image");
        dsType = (String) data.get("img_datastream");
        pageUuid = (String) data.get("page_uuid");
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
    }

    @Override
    public TerminationReason process() throws Exception {
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
        return null;
    }
}
