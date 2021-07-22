package cz.mzk.fofola.model.process;

import cz.mzk.fofola.process.TestProcess;
import cz.mzk.fofola.process.check_donator.CheckDonatorProcess;
import cz.mzk.fofola.process.dnnt.DnntLabelLinkingProcess;
import cz.mzk.fofola.process.donator_linker.DonatorLinkerProcess;
import cz.mzk.fofola.process.img_editing.ImgEditingProcess;
import cz.mzk.fofola.process.perio_parts_publishing.PerioPartsPublishingProcess;
import cz.mzk.fofola.process.solr_response.SolrResponseWritingProcess;
import cz.mzk.fofola.process.vc_linker.VCLinkerProcess;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ProcessType {

    TEST("Process for testing purposes", "test", TestProcess.class),
    VC_LINKING("Virtual collection linking process", "vc_link", VCLinkerProcess.class),
    IMG_EDITION_PROCESS("Image edition process", "img_editing", ImgEditingProcess.class),
    DONATOR_LINKING("Donator linking process", "donator_link", DonatorLinkerProcess.class),
    PERIO_PARTS_PUBLISHING("Periodical parts publishing process", "perio_parts_pub", PerioPartsPublishingProcess.class),
    DONATOR_CHECKING("Process to check if books in VC has donator node in RELS-EXT", "donator_check", CheckDonatorProcess.class),
    SOLR_RESPONSE("Process that writes uuids from the Solr response to a file", "solr-response", SolrResponseWritingProcess.class),
    DNNT_LABEL_LINKING("DNNT label linking process", "dnnt_link", DnntLabelLinkingProcess.class);

    private final String description;
    private final String alias;
    private final Class processClass;

    public static ProcessType findByAlias(String alias){
        for(ProcessType t : values()){
            if(t.alias.equals(alias)){
                return t;
            }
        }
        return null;
    }
}
