package mops;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bewerbung2")
public class OrgaController {

    @GetMapping("/overview")
    public String orga() {
        return "overview";
    }
}
