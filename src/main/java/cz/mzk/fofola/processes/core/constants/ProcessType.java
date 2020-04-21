package cz.mzk.fofola.processes.core.constants;

import cz.mzk.fofola.processes.TestProcess;
import cz.mzk.fofola.processes.vc_linker.VCLinkerProcess;

public enum ProcessType {

    TEST("Process for testing purposes", "test", TestProcess.class),
    VC_LINKING("Virtual collection linking process", "vc_link", VCLinkerProcess.class);

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