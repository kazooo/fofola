package cz.mzk.fofola.service;

import cz.mzk.fofola.api.fedora.*;
import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.model.vc.VirtualCollection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class VcService {

    private final KrameriusApi krameriusApi;
    private final RIApi resourceIndexApi;
    private final FedoraApi fedoraApi;

    /**
     * There are three ways how to get virtual collections from Kramerius:
     *
     *  - /search/api/v5.0/vc
     *    get a virtual collection list from Solr using facets,
     *    fast but incomplete response, because doesn't contain any empty collections
     *
     *  - /search/api/v5.0/admin/vc
     *    get a virtual collection list from Fedora using triples,
     *    should be fast and complete, but due to some reasons
     *    Kramerius aggregates virtual collections VERY slow
     *
     *  - fast custom solution, that uses Fedora's data streams and triples directly avoiding Kramerius querying
     */
    public List<VirtualCollection> getAllVcs() throws IOException {
        final RIQuery query = RIQuery.builder()
                .object(RIQuery.ANY)
                .relation(RIRelation.HAS_MODEL)
                .subject(RIModel.COLLECTION)
                .build();
        final List<RITriple> collectionTriples = resourceIndexApi.query(query);
        return collectionTriples.stream()
                .map(this::toVc)
                .toList();
    }

    private VirtualCollection toVc(final RITriple triple) {
        final String uuid = triple.getObject();
        return VirtualCollection.builder()
                .uuid(uuid)
                .nameCz(fedoraApi.getTextCz(uuid))
                .nameEn(fedoraApi.getTextEn(uuid))
                .descriptionCz(fedoraApi.getLongTextCz(uuid))
                .descriptionEn(fedoraApi.getLongTextEn(uuid))
                .build();
    }

    public String createVc(final VirtualCollection virtualCollection) throws IOException {
        final VC newVirtualCollection = krameriusApi.createEmptyVc();
        virtualCollection.setUuid(newVirtualCollection.getPid());
        return updateVc(virtualCollection);
    }

    public String updateVc(final VirtualCollection virtualCollection) throws IOException {
        final String uuid = virtualCollection.getUuid();

        fedoraApi.setTextCz(uuid, virtualCollection.getNameCz());
        fedoraApi.setTextEn(uuid, virtualCollection.getNameEn());
        fedoraApi.setLongTextCz(uuid, virtualCollection.getDescriptionCz());
        fedoraApi.setLongTextEn(uuid, virtualCollection.getDescriptionEn());

        if (virtualCollection.getFullImg() != null) {
            fedoraApi.setFullImg(uuid, virtualCollection.getFullImg());
        }
        if (virtualCollection.getThumbImg() != null) {
            fedoraApi.setThumbnailImg(uuid, virtualCollection.getThumbImg());
        }
        return uuid;
    }

    public String deleteVc(final String uuid) {
        return krameriusApi.deleteVc(uuid);
    }
}
