package cz.mzk.fofola.service;

import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

@Slf4j
public class IpLogger {
    public static void logIp(String ip, String message) {
        log.info("[" + ip + "] " + message);
    }
}
