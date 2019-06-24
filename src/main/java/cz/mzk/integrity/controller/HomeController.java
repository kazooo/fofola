package cz.mzk.integrity.controller;

import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/examine_pid")
    public String examineForm() {
        return "examine";
    }

    @PostMapping("/examine_pid")
    public String examinePID(@RequestParam(name = "uuid") String uuid, HttpServletRequest request) {
        logger.info("get uuid: " + uuid);
        return "redirect:/examine_pid";
    }
}
