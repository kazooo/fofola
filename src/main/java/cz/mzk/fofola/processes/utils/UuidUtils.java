package cz.mzk.fofola.processes.utils;

public class UuidUtils {

    public static String checkAndMakeUuid(String uuid) {
        return uuid.startsWith("uuid:") ? uuid : "uuid:" + uuid;
    }

    public static String checkAndMakeVcId(String vc) {
        return vc.startsWith("vc:") ? vc : "vc:" + vc;
    }
}
