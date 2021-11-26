package cz.mzk.fofola.controller;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.model.vc.VirtualCollection;
import cz.mzk.fofola.request.CreateVcRequest;
import cz.mzk.fofola.request.UpdateVcRequest;
import cz.mzk.fofola.service.VcService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/vc")
@AllArgsConstructor
@Slf4j
public class VcController {

    private final KrameriusApi krameriusApi;
    private final VcService vcService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<VirtualCollection> getAllVcs() {
        log.info("Got a request to return all virtual collections in Kramerius.");

        final List<VC> vcList = krameriusApi.getAllVirtualCollections();
        final List<VirtualCollection> virtualCollections = new ArrayList<>();
        vcList.forEach(vc -> virtualCollections.add(VirtualCollection.from(vc)));
        virtualCollections.sort(Comparator.comparing(VirtualCollection::getNameCz));
        return virtualCollections;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String createVc(@RequestPart(value = "vcData") final CreateVcRequest createVcRequest,
                                           @RequestPart(value = "fullImg", required = false) final MultipartFile fullImg,
                                           @RequestPart(value = "thumbImg", required = false) final MultipartFile thumbImg) throws IOException {
        log.info(String.format(
                "Got a request to create a new virtual collection in Kramerius with parameters: " +
                        "name (cz): %s, (en): %s, descriptions (cz): %s, (en): %s, with FULL img: %s, with THUMB img: %s",
                createVcRequest.getNameCz(), createVcRequest.getNameEn(),
                createVcRequest.getDescriptionCz(), createVcRequest.getDescriptionEn()), fullImg != null, thumbImg != null);

        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(createVcRequest, virtualCollection);
        virtualCollection.setFullImg(fullImg);
        virtualCollection.setThumbImg(thumbImg);
        return vcService.createVc(virtualCollection);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String updateVc(@RequestBody final UpdateVcRequest updateVcRequest) throws IOException {
        log.info(String.format(
                "Got a request to update an existing virtual collection in Kramerius with parameters: " +
                        "uuid: %s, name (cz): %s, (en): %s, descriptions (cz): %s, (en): %s",
                updateVcRequest.getUuid(), updateVcRequest.getNameCz(), updateVcRequest.getNameEn(),
                updateVcRequest.getDescriptionCz(), updateVcRequest.getDescriptionEn()));

        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(updateVcRequest, virtualCollection);
        return vcService.updateVc(virtualCollection);
    }
}
