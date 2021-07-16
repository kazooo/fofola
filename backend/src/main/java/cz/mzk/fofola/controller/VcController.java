package cz.mzk.fofola.controller;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.vc.VC;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/vc")
@AllArgsConstructor
public class VcController {

    private final KrameriusApi krameriusApi;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getAllVcs() {
        return getSortedVirtualCollection();
    }

    private List<Map<String, String>> getSortedVirtualCollection() {
        final List<VC> vcList = krameriusApi.getVirtualCollections();
        final List<Map<String, String>> vcNameUuid = new ArrayList<>();

        vcList.forEach(vc -> vcNameUuid.add(
                new HashMap<>() {{
                    put("name_cs", vc.descs.cs);
                    put("uuid", vc.pid);
                }}
        ));

        vcNameUuid.sort(Comparator.comparing(vc -> vc.get("name_cs")));

        return vcNameUuid;
    }
}
