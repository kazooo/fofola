package cz.mzk.fofola.process.internal.img_editing;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.doc.Datastreams;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import org.springframework.web.multipart.MultipartFile;
import cz.mzk.fofola.model.process.Process;

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
        FedoraApi fedoraApi = new FedoraApi(fedoraHost, fedoraUser, fedoraPswd);
        Datastreams datastream = Datastreams.getDataStream(dsType);
        if (datastream == null)
            throw new IllegalStateException("Can't determine data stream type for name: " + dsType);
        if (datastream == Datastreams.THUMB_IMG) {
            fedoraApi.setThumbnailImg(pageUuid, image);
        } else if (datastream == Datastreams.FULL_IMG) {
            fedoraApi.setFullImg(pageUuid, image);
        } else {
            throw new IllegalStateException("Data stream " + dsType + " is not an image data stream!");
        }
        return null;
    }
}
