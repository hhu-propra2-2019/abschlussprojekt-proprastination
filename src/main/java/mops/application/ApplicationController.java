package mops.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bewerbung2")
public class ApplicationController {

    @GetMapping("/")
    public String main(){

        return "main";
    }

   @GetMapping("/personal")
    public String personal(){

        return "personal";
    }
}
