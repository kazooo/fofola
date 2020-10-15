package cz.mzk.fofola.service;

public class UuidService {

    public static String checkAndMakeUuid(String uuid) {
        return uuid.startsWith("uuid:") ? uuid : "uuid:" + uuid;
    }
}
