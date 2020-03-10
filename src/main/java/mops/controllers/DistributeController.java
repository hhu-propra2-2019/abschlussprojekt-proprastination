package mops.controllers;

import mops.organization.webclasses.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bewerbung2")
public class DistributeController {

    /**
     * Method for creating an Account
     *
     * @param token The KeycloakAuthentication
     * @return The Users Account
     */

    private Account createAccountFromPrincipal(final KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    /**
     * The GetMapping to redirect users in dependence of their role
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_orga') or hasRole('ROLE_studentin')")
    public String index(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (token.getAccount().getRoles().contains("ROLE_orga")) {
            return "redirect:/bewerbung2/organizator/";
        } else {
            return "redirect:/bewerbung2/applicant/";
        }
    }

}
