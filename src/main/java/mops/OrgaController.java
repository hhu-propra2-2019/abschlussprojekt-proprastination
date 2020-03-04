package mops;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrgaController {

    @GetMapping("/orgaOverview")
    public String index(){
        return "orgaOverview";
    }
}
