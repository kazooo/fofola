package cz.mzk.fofola.service;

public class UuidService {

    public static String makeUuid(String uuid) {
        uuid = uuid.replaceAll("\\s+","");
        return uuid.startsWith("uuid:") ? uuid : "uuid:" + uuid;
    }
}
