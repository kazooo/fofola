package cz.mzk.integrity.threads;

import org.springframework.context.ApplicationEvent;

public class FofolaThreadEvent extends ApplicationEvent {

    public static final String CHECK_SOLR_FINISH = "check_solr_finish";
    public static final String GEN_SITEMAPS_FINISH = "gen_sitemaps_finish";

    public static final String CHECK_SOLR_EXC = "check_solr_exc";
    public static final String GEN_SITEMAPS_EXC = "gen_sitemaps_exc";

    private String type;
    private String message;

    FofolaThreadEvent(Object source, String t, String m) {
        super(source);
        type = t;
        message = m;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
