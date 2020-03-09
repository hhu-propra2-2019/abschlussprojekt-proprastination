package mops.application.controllers;

import mops.organization.webclasses.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bewerbung2")
public class ApplicationController {

    @GetMapping("/applicant")
    @Secured("ROLE_studentin")
    public String main(){

        return "main";
    }

    @GetMapping("/application")
    public String newAppl(){
        return "applicationPersonal";
    }

    @GetMapping("/openAppl")
    public String openAppl(){

        return "openAppl";
    }

    @GetMapping("/personal")
    public String personal(){

        return "personal";
    }

    @GetMapping("/module")
    public String module(){
        return "applicationModule";
    }
}
