package cz.mzk.fofola.controller;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.model.vc.VirtualCollection;
import cz.mzk.fofola.request.CreateVcRequest;
import cz.mzk.fofola.request.UpdateVcRequest;
import cz.mzk.fofola.service.VcService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/vc")
@AllArgsConstructor
public class VcController {

    private final KrameriusApi krameriusApi;
    private final VcService vcService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<VirtualCollection> getAllVcs() {
        final List<VC> vcList = krameriusApi.getVirtualCollections();
        final List<VirtualCollection> virtualCollections = new ArrayList<>();
        vcList.forEach(vc -> virtualCollections.add(VirtualCollection.from(vc)));
        virtualCollections.sort(Comparator.comparing(VirtualCollection::getNameCz));
        return virtualCollections;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createVc(@RequestPart(value = "vcData") final CreateVcRequest createVcRequest,
                                           @RequestPart(value = "fullImg", required = false) final MultipartFile fullImg,
                                           @RequestPart(value = "thumbImg", required = false) final MultipartFile thumbImg) {
        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(createVcRequest, virtualCollection);
        virtualCollection.setFullImg(fullImg);
        virtualCollection.setThumbImg(thumbImg);
        final String uuid = vcService.createVc(virtualCollection);
        return ResponseEntity.ok(uuid);
    }

    @PutMapping
    public ResponseEntity<String> updateVc(@RequestBody final UpdateVcRequest updateVcRequest) throws IOException {
        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(updateVcRequest, virtualCollection);
        final String uuid = vcService.updateVc(virtualCollection);
        return ResponseEntity.ok(uuid);
    }
}
