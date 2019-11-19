package cz.mzk.fofola.controller;

import cz.mzk.fofola.configuration.StartupApplicationListener;
import cz.mzk.fofola.service.IpLogger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry Fofola home page.");
        model.addAttribute("up_time", StartupApplicationListener.appStartupTime);
        return "index";
    }
}
