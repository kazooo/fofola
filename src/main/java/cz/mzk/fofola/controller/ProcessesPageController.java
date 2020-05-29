package cz.mzk.fofola.controller;

import cz.mzk.fofola.service.IpLogger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProcessesPageController {

    @GetMapping("/link_vc")
    public String getVcLinkingPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry VC linking section.");
        return "link_vc";
    }

    @GetMapping("/perio-parts-publish")
    public String getPerioPartsPublishingPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry private periodical parts publishing section.");
        return "perio_parts_publishing";
    }

    @GetMapping("/link-donator")
    public String getDonatorLinkingPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry donator linking section.");
        return "link_donator";
    }
}
