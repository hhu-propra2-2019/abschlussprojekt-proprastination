package mops.controllers;

import mops.services.DistributeService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;


@SessionScope
@Controller
@RequestMapping("/bewerbung2")
public class DistributeController {

    private final DistributeService distributeService;

    /**
     * Injects the Service
     * @param distributeService the service handling the logic determining where to redirect somebody
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DistributeController(final DistributeService distributeService) {
        this.distributeService = distributeService;
    }

    /**
     * The GetMapping to redirect users in dependence of their role
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_orga', 'ROLE_studentin', 'ROLE_verteiler', 'ROLE_setup')")
    public String index(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", distributeService.createAccountFromPrincipal(token));
        }
        return distributeService.getRedirectForRole(token);
    }
}
