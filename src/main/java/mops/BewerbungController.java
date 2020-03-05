package mops;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bewerbung2")

public class BewerbungController {

    @GetMapping("/bewerbung")
    public String bewerbung() {
        return "bewerbung";
    }
}
