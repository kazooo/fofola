package cz.mzk.fofola.process.img_editing;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
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
    private final FofolaConfiguration configuration;

    public ImgEditingProcess(ProcessParams params) throws IOException {
        super(params);
        configuration = params.getConfig();
        Map<String, ?> data = params.getData();

        image = (MultipartFile) data.get("image");
        dsType = (String) data.get("img_datastream");
        pageUuid = (String) data.get("page_uuid");
    }

    @Override
    public TerminationReason process() throws Exception {
        final FedoraApi fedoraApi = ApiConfiguration.getFedoraApi(configuration);
        final Datastreams datastream = Datastreams.getDataStream(dsType);
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
