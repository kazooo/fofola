package cz.mzk.fofola.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VcController {

    @GetMapping("/link_vc")
    public String getVcLinkingPage() {
        return "link_vc";
    }

}
