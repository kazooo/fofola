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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<String> createVc(@RequestBody final CreateVcRequest createVcRequest) {
        final VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(createVcRequest, virtualCollection);
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
