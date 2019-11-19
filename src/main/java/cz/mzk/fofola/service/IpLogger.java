package cz.mzk.fofola.service;

import java.util.logging.Logger;

public class IpLogger {


    private static final Logger logger = Logger.getLogger(IpLogger.class.getName());

    public static void logIp(String ip, String message) {
        logger.info("[" + ip + "] " + message);
    }

}
