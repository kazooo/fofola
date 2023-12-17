package cz.mzk.fofola.process.donator_linker;

import cz.mzk.fofola.configuration.AppProperties;
import java.util.logging.Logger;

public class DonatorLinker {

    private final int maxDocsPerQuery;
    private final Logger logger;

    public DonatorLinker(final AppProperties props, int maxDocsPerQuery, Logger logger) {
        this.logger = logger;
        this.maxDocsPerQuery = maxDocsPerQuery;
    }

    public void link(String rootUuid, String donator) {

    }

    public void unlink(String rootUuid, String donator) {

    }

    public void commitAndClose() {

    }
}
