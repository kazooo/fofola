package cz.mzk.fofola.process.img_editing;

import cz.mzk.fofola.configuration.AppProperties;
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
    private final AppProperties configuration;

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
        return null;
    }
}
