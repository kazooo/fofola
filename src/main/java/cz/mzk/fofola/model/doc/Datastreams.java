package cz.mzk.fofola.model.doc;

public enum Datastreams {

    RELS_EXT("RELS-EXT", "application/rdf+xml", "false", "X", "A"),
    DC("DC", "text/xml", "false", "X", "A"),
    TEXT_EN("TEXT_en", "text/plain", "false", "M", "A"),
    TEXT_CS("TEXT_cs", "text/plain", "false", "M", "A"),
    LONG_TEXT_EN("LONG_TEXT_en", "text/plain", "false", "M", "A"),
    LONG_TEXT_CS("LONG_TEXT_cs", "text/plain", "false", "M", "A"),
    THUMB_IMG("IMG_THUMB", "image/jpeg", "true", "M", "A"),
    FULL_IMG("IMG_FULL", "image/jpeg", "true", "M", "A");

    public final String name;
    public final String mimeType;
    public final String versionable;
    public final String controlGroup;
    public final String state;

    Datastreams(String dsStrName, String dsMimeType,
                String versionable, String controlGroup, String state) {
        this.name = dsStrName;
        this.mimeType = dsMimeType;
        this.versionable = versionable;
        this.controlGroup = controlGroup;
        this.state = state;
    }

    public static Datastreams getDataStream(String dsName) {
        for (Datastreams ds : Datastreams.values()) {
            if (ds.name.equals(dsName)) {
                return ds;
            }
        }
        return null;
    }
}