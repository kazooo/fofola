package cz.mzk.fofola.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class UuidStateResponse {

    private final String uuid;
    private boolean isIndexed;
    private boolean isStored;

    private String accessibilityInSolr;
    private String accessibilityInFedora;

    private String model;
    private String rootTitle;

    private String imgUrl;
    private String solrModifiedDate;
    private String fedoraModifiedDate;
}
