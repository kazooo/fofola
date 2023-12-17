package cz.mzk.fofola.api;

import cz.mzk.fofola.api.processes.KrameriusIndexationType;
import cz.mzk.fofola.api.processes.KrameriusProcessDef;
import cz.mzk.fofola.api.processes.KrameriusProcessDefType;
import cz.mzk.fofola.model.kprocess.KrameriusBatchResponse;
import cz.mzk.fofola.model.kprocess.KrameriusExtendedProcess;
import cz.mzk.fofola.model.kprocess.KrameriusProcess;
import cz.mzk.fofola.utils.ApiUtils;
import cz.mzk.fofola.configuration.AppProperties;
import cz.mzk.fofola.configuration.WebClientConfiguration;
import cz.mzk.fofola.model.vc.RawVc;
import cz.mzk.fofola.model.vc.RawVcList;
import cz.mzk.fofola.model.vc.VirtualCollection;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class KrameriusApi {

    private final String host;
    private final RestTemplate restTemplate;
    private final HttpHeaders commonHeaders;
    private final HttpEntity<String> commonHttpEntity;

    private static final String CLIENT_API_V5 = "/search/api/v5.0";
    private static final String ADMIN_API_V7 = "/search/api/admin/v7.0";

    private static final boolean DEFAULT_VC_CAN_LEAVE_FLAG = true;

    public KrameriusApi(final AppProperties config, final RestTemplate restTemplate) {
        host = config.getKrameriusHost();
        this.restTemplate = restTemplate;
        commonHeaders = WebClientConfiguration.createCommonHeaders();
        commonHttpEntity = new HttpEntity<>(commonHeaders);
    }

    private KrameriusProcess planProcess(KrameriusProcessDef processDef) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "processes")
                .build().toString();

        HttpEntity<KrameriusProcessDef> requestEntity = new HttpEntity<>(processDef, commonHeaders);
        ResponseEntity<KrameriusProcess> response = restTemplate.postForEntity(url, requestEntity, KrameriusProcess.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.CREATED) {
            throw new IllegalStateException(String.format("Creating the process with id [%s] failed with status code [%s]", processDef.getDefId(), status));
        }

        return response.getBody();
    }

    public KrameriusProcess getProcess(String uuid) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "processes", "by_process_uuid", uuid)
                .build().toString();

        final ResponseEntity<KrameriusExtendedProcess> response = restTemplate.exchange(
                url, HttpMethod.GET, commonHttpEntity, KrameriusExtendedProcess.class
        );

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Can't get the process with uuid [%s], failed with status code [%s]", uuid, status));
        }

        final KrameriusExtendedProcess extendedProcess = response.getBody();
        return extendedProcess == null ? null : extendedProcess.getProcess();
    }

    public List<KrameriusProcess> getProcesses(final MultiValueMap<String, String> params) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "processes", "batches")
                .queryParams(params)
                .build().toString();

        final ResponseEntity<KrameriusBatchResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, commonHttpEntity, KrameriusBatchResponse.class
        );

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Can't get Kramerius processes, failed with status code [%s]", status));
        }

        final KrameriusBatchResponse batchResponse = response.getBody();
        return batchResponse == null ? null :
                batchResponse.getBatches().stream().flatMap(b -> b.getProcesses().stream()).collect(Collectors.toList());
    }

    public void stopProcess(final String processId) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "processes", "batches", "by_first_process_id", processId, "execution")
                .build().toString();

        final ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, commonHttpEntity, Void.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Can't stop the process with id [%s], failed with status code [%s]", processId, status));
        }
    }

    public void removeProcess(String processId) {
        String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "processes", "batches", "by_first_process_id", processId)
                .build().toString();

        final ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, commonHttpEntity, Void.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Can't delete the process with id [%s], failed with status code [%s]", processId, status));
        }
    }

    public List<VirtualCollection> getAllVcs() {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "collections")
                .build().toString();

        ResponseEntity<RawVcList> response = restTemplate.exchange(url, HttpMethod.GET, commonHttpEntity, RawVcList.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Can't get all virtual collections, failed with status code [%s]", status));
        }

        RawVcList rawVcList = Objects.requireNonNull(response.getBody());
        return rawVcList.getCollections().stream().map(this::rawToNormal).collect(Collectors.toList());
    }

    public VirtualCollection createVc(final VirtualCollection vc) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "collections")
                .build().toString();

        final VcDataRequest request = VcDataRequest.builder()
                .name(vc.getNameCz())
                .content(vc.getDescriptionCz())
                .description(vc.getDescriptionCz())
                .standalone(DEFAULT_VC_CAN_LEAVE_FLAG)
                .build();

        final HttpHeaders headers = new HttpHeaders(SerializationUtils.clone(commonHeaders));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        final HttpEntity<VcDataRequest> requestAuthEntity = new HttpEntity<>(request, headers);
        ResponseEntity<RawVc> response = restTemplate.postForEntity(url, requestAuthEntity, RawVc.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.CREATED) {
            throw new IllegalStateException(String.format("Creating virtual collection with the name [%s] failed with status code [%s]", vc.getNameCz(), status));
        }

        return rawToNormal(Objects.requireNonNull(response.getBody()));
    }

    public void updateVc(final VirtualCollection vc) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "collections", vc.getUuid())
                .build().toString();

        final VcDataRequest request = VcDataRequest.builder()
                .name(vc.getNameCz())
                .content(vc.getDescriptionCz())
                .description(vc.getDescriptionCz())
                .standalone(DEFAULT_VC_CAN_LEAVE_FLAG)
                .build();

        final HttpHeaders headers = new HttpHeaders(SerializationUtils.clone(commonHeaders));
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<VcDataRequest> requestAuthEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestAuthEntity, Void.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Creating virtual collection with the name [%s] failed with status code [%s]", vc.getNameCz(), status));
        }
    }

    public void addToVc(final String vcId, final String rootUuid) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "collections", vcId, "items")
                .build().toString();

        final AddUuidToVcRequest request = AddUuidToVcRequest.builder()
                .uuid(rootUuid)
                .build();

        final HttpHeaders headers = new HttpHeaders(SerializationUtils.clone(commonHeaders));
        headers.setContentType(MediaType.TEXT_PLAIN);

        final HttpEntity<AddUuidToVcRequest> requestAuthEntity = new HttpEntity<>(request, headers);
        final ResponseEntity<Void> response = restTemplate.postForEntity(url, requestAuthEntity, Void.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.CREATED) {
            throw new IllegalStateException(String.format("Adding [%s] to [%s] failed with status code [%s]", rootUuid, vcId, status));
        }
    }

    public void removeFromVc(final String vcId, final String rootUuid) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "collections", vcId, "items", rootUuid)
                .build().toString();

        final ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, commonHttpEntity, Void.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Removing [%s] from [%s] failed with status code [%s]", rootUuid, vcId, status));
        }
    }

    public void deleteVc(final String vcId) {
        final String url = UriComponentsBuilder.fromHttpUrl(host)
                .pathSegment(ADMIN_API_V7, "collections", vcId)
                .build().toString();

        final ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, commonHttpEntity, Void.class);

        final HttpStatus status = response.getStatusCode();
        if (status != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Removing [%s] failed with status code [%s]", vcId, status));
        }
    }

    public KrameriusProcess makePublic(final String uuid, final String scope) {
        return changeAccessibility(uuid, "PUBLIC", scope);
    }

    public KrameriusProcess makePrivate(final String uuid, final String scope) {
        return changeAccessibility(uuid, "PRIVATE", scope);
    }

    private KrameriusProcess changeAccessibility(final String uuid, final String accessibility, final String scope) {
        final KrameriusProcessDef processDef = KrameriusProcessDef.builder()
                .defId(KrameriusProcessDefType.SET_POLICY)
                .params(Map.of(
                        "pid", uuid,
                        "policy", accessibility,
                        "scope", scope
                ))
                .build();
        return planProcess(processDef);
    }

    public KrameriusProcess reindexDoc(final String uuid) {
        final KrameriusProcessDef processDef = KrameriusProcessDef.builder()
                .defId(KrameriusProcessDefType.FULL_OBJECT_INDEX)
                .params(Map.of(
                        "pid", uuid,
                        "type", KrameriusIndexationType.TREE_AND_FOSTER_TREES,
                        "ignoreInconsistentObjects", true
                ))
                .build();
        return planProcess(processDef);
    }

    public KrameriusProcess deleteDoc(final String uuid) {
        final KrameriusProcessDef processDef = KrameriusProcessDef.builder()
                .defId(KrameriusProcessDefType.DELETE_TREE)
                .params(Map.of(
                        "pid", uuid
                ))
                .build();
        return planProcess(processDef);
    }

    public void generateAndDownloadPDF(Map<String, String> params, String outFilePath) throws IOException {
        String url = host + CLIENT_API_V5 + "/pdf/parent";
        url = ApiUtils.buildUri(url, params);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, commonHttpEntity, byte[].class);
        final Path outputPDFFile = Paths.get(outFilePath);
        Files.createDirectories(outputPDFFile.getParent());
        Files.write(outputPDFFile, Objects.requireNonNull(response.getBody()));
    }

    @Data
    @Builder
    private static class VcDataRequest {
        private String name;
        private String description;
        private String content;
        private boolean standalone;
    }

    @Data
    @Builder
    private static class AddUuidToVcRequest {
        private String uuid;
    }

    private VirtualCollection rawToNormal(final RawVc rawVc) {
        return new VirtualCollection()
                .setUuid(rawVc.getPid())
                .setNameCz(rawVc.getNames().get("cze"))
                .setNameEn(rawVc.getNames().get("eng"))
                .setDescriptionCz(rawVc.getDescriptions().get("cze"))
                .setDescriptionEn(rawVc.getDescriptions().get("eng"));
    }
}
