package cz.mzk.fofola.processes.core.constants;

import cz.mzk.fofola.processes.internal.TestProcess;
import cz.mzk.fofola.processes.internal.check_donator.CheckDonatorProcess;
import cz.mzk.fofola.processes.internal.donator_linker.DonatorLinkerProcess;
import cz.mzk.fofola.processes.internal.img_editing.ImgEditingProcess;
import cz.mzk.fofola.processes.internal.perio_parts_publishing.PerioPartsPublishingProcess;
import cz.mzk.fofola.processes.internal.vc_linker.VCLinkerProcess;

public enum ProcessType {

    TEST("Process for testing purposes", "test", TestProcess.class),
    VC_LINKING("Virtual collection linking process", "vc_link", VCLinkerProcess.class),
    IMG_EDITION_PROCESS("Image edition process", "img_editing", ImgEditingProcess.class),
    DONATOR_LINKING("Donator linking process", "donator_link", DonatorLinkerProcess.class),
    PERIO_PARTS_PUBLISHING("Periodical parts publishing process", "perio_parts_pub", PerioPartsPublishingProcess.class),
    DONATOR_CHECKING("Process to check if books in VC has donator node in RELS-EXT", "donator_check", CheckDonatorProcess.class);

    private final String description;
    private final String alias;
    private final Class processClass;

    ProcessType(String desc, String alias, Class processClass) {
        description = desc;
        this.alias = alias;
        this.processClass = processClass;
    }

    public String getDescription() {
        return description;
    }

    public Class getProcessClass() {
        return processClass;
    }

    public static ProcessType findByAlias(String alias){
        for(ProcessType t : values()){
            if(t.alias.equals(alias)){
                return t;
            }
        }
        return null;
    }
}
