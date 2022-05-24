package cz.mzk.fofola.rest;

import cz.mzk.fofola.model.vc.VirtualCollection;
import cz.mzk.fofola.rest.request.vc.CreateVcRequest;
import cz.mzk.fofola.rest.request.vc.DeleteVcRequest;
import cz.mzk.fofola.rest.request.vc.UpdateVcRequest;
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

    private final VcService vcService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<VirtualCollection> getAllVcs() throws IOException {
        log.info("Got a request to return all virtual collections.");

        final List<VirtualCollection> virtualCollections = vcService.getAllVcs();
        return virtualCollections.stream()
                .peek(vc -> {
                    if (vc.getNameCz() == null) {
                        vc.setNameCz("Unnamed virtual collection");
                    }
                })
                .sorted(Comparator.comparing(VirtualCollection::getNameCz))
                .toList();
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String createVc(@RequestPart(value = "vcData") final CreateVcRequest createVcRequest,
                           @RequestPart(value = "fullImg", required = false) final MultipartFile fullImg,
                           @RequestPart(value = "thumbImg", required = false) final MultipartFile thumbImg) throws IOException {
        log.info(String.format(
                "Got a request to create a new virtual collection with parameters: " +
                        "name (cz): %s, (en): %s, descriptions (cz): %s, (en): %s, with FULL img: %s, with THUMB img: %s",
                createVcRequest.getNameCz(), createVcRequest.getNameEn(),
                createVcRequest.getDescriptionCz(), createVcRequest.getDescriptionEn(), fullImg != null, thumbImg != null));

        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(createVcRequest, virtualCollection);
        virtualCollection.setFullImg(fullImg);
        virtualCollection.setThumbImg(thumbImg);
        return vcService.createVc(virtualCollection);
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String updateVc(@RequestPart(value = "vcData") final UpdateVcRequest updateVcRequest,
                           @RequestPart(value = "fullImg", required = false) final MultipartFile fullImg,
                           @RequestPart(value = "thumbImg", required = false) final MultipartFile thumbImg) throws IOException {
        log.info(String.format(
                "Got a request to update an existing virtual collection with parameters: " +
                        "uuid: %s, name (cz): %s, (en): %s, descriptions (cz): %s, (en): %s, with FULL img: %b, with THUMB img: %b",
                updateVcRequest.getUuid(), updateVcRequest.getNameCz(), updateVcRequest.getNameEn(),
                updateVcRequest.getDescriptionCz(), updateVcRequest.getDescriptionEn(), fullImg != null, thumbImg != null));

        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(updateVcRequest, virtualCollection);
        virtualCollection.setFullImg(fullImg);
        virtualCollection.setThumbImg(thumbImg);
        return vcService.updateVc(virtualCollection);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteVc(@RequestBody final DeleteVcRequest deleteVcRequest) {
        log.info(String.format(
                "Got a request to delete an existing virtual collection with parameters: uuid: %s, name (cz): %s, (en): %s",
                deleteVcRequest.getUuid(), deleteVcRequest.getNameCz(), deleteVcRequest.getNameEn())
        );
        return vcService.deleteVc(deleteVcRequest.getUuid());
    }
}
