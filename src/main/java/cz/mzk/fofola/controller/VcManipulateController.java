package cz.mzk.fofola.controller;

import cz.mzk.fofola.dto.request.CreateVcRequest;
import cz.mzk.fofola.dto.request.UpdateVcRequest;
import cz.mzk.fofola.model.vc.VirtualCollection;
import cz.mzk.fofola.service.VcService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/vc")
@AllArgsConstructor
public class VcManipulateController {

    private final VcService vcService;

    @PostMapping
    public ResponseEntity<String> createVc(@RequestBody CreateVcRequest createVcRequest) {
        VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(createVcRequest, virtualCollection);
        String uuid = vcService.createVc(virtualCollection);
        return ResponseEntity.ok(uuid);
    }

    @PutMapping
    public ResponseEntity<String> updateVc(@RequestBody UpdateVcRequest updateVcRequest) throws IOException {
        VirtualCollection virtualCollection = new VirtualCollection();
        BeanUtils.copyProperties(updateVcRequest, virtualCollection);
        String uuid = vcService.updateVc(virtualCollection);
        return ResponseEntity.ok(uuid);
    }
}
