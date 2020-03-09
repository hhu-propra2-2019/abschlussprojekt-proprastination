package mops.application.controllers;

import mops.organization.webclasses.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/bewerbung2/applicant")
public class ApplicationController {

    private Account createAccountFromPrincipal(final KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    @GetMapping("/")
    @Secured("ROLE_studentin")
    public String main(final KeycloakAuthenticationToken token, final Model model){
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "applicantMain";
    }

    @GetMapping("/application")
    @Secured("ROLE_studentin")
    public String newAppl(final KeycloakAuthenticationToken token, final Model model){
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "applicationPersonal";
    }

    @GetMapping("/openAppl")
    public String openAppl(final KeycloakAuthenticationToken token, final Model model){
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "openAppl";
    }

    @GetMapping("/personal")
    public String personal(final KeycloakAuthenticationToken token, final Model model){
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "personal";
    }

    @GetMapping("/module")
    public String module(final KeycloakAuthenticationToken token, final Model model){
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "applicationModule";
    }

    @GetMapping("/logout")
    public String logout(final HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }
}
