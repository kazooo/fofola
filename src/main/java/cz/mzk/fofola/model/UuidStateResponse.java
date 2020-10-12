package cz.mzk.fofola.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UuidStateResponse {

    private String uuid;
    private boolean isIndexed;
    private boolean isStored;

    private String accessibilityInSolr;
    private String accessibilityInFedora;

    private String model;
    private String rootTitle;

    private String imgUrl;
    private String solrModifiedDate;
    private String fedoraModifiedDate;

    public UuidStateResponse(String uuid) {
        this.uuid = uuid;
    }
}
