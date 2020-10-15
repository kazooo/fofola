package cz.mzk.fofola.process.utils;

public class UuidUtils {

    public static String checkAndMakeUuid(String uuid) {
        return uuid.startsWith("uuid:") ? uuid : "uuid:" + uuid;
    }

    public static String wrapStr(String str) { return "\"" + str + "\""; }
}
