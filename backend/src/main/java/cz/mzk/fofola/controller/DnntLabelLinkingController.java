package cz.mzk.fofola.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/link-dnnt")
@AllArgsConstructor
public class DnntLabelLinkingController {

    @GetMapping
    public String getDnntLabelLinkingPage() {
        return "link-dnnt";
    }
}
