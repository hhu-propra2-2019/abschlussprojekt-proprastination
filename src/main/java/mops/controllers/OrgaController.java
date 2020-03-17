package mops.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import mops.model.Account;
import mops.services.ApplicantService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.awt.desktop.AppReopenedListener;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@SessionScope
@Controller
@RequestMapping("/bewerbung2/organisator")
public class OrgaController {

    @Autowired
    private ApplicantService applicantService;

    private Account createAccountFromPrincipal(final KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    /**
     * The GepMapping for the main page
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */

    @GetMapping("/")
    @Secured("ROLE_orga")
    public String index(final KeycloakAuthenticationToken token, final Model model) throws JsonProcessingException {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "organizer/orgaMain";
    }

    /**
     * PostMapping to save changes
     * @param token
     * @param model
     * @param priority
     * @param hours
     * @param comment
     * @return orgaMain.html rendered as a String
     * @throws JsonProcessingException
     */
    @PostMapping("/")
    //@Secured("ROLE orga")
    public String save(final KeycloakAuthenticationToken token, final Model model,
                       @RequestParam("priority") final String priority,
                       @RequestParam("hours") final String hours,
                       @RequestParam("comment") final String comment) throws JsonProcessingException {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "organizer/orgaMain";
    }


    /**
     * Shows overview of applications for a module.
     * @param token
     * @param model
     * @return orgaOverview.html as String
     */
    @GetMapping("/overview")
    @Secured("ROLE_orga")
    public String overview(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "organizer/orgaOverview";
    }

    /**
     * Needed to display additional information about each application on the overview page.
     * (Inside a modal / popup window.)
     * @return "applicationModalContent", the HTML file with the modal content.
     */
    @GetMapping("/modal")
    @Secured("ROLE_orga")
    public String applicationInfo() {
        return "organizer/applicationModalContent";
    }

}
