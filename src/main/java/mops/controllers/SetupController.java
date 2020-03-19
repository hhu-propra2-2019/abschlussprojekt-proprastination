package mops.controllers;

import mops.model.Account;
import mops.model.classes.Module;
import mops.services.CSVService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/setup")
public class SetupController {

    private Account createAccountFromPrincipal(final KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    /**
     * Get Mapping for the mainboard
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/")
    @Secured("ROLE_setup")
    public String index(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            List<String[]> modules = CSVService.getModulesWithDetails();
            List<Module> moduleList = new ArrayList<Module>();
            String[] tmp;
            for (int i = 0; i < modules.size(); i++) {
                tmp = modules.get(i);
                Module newModule = new Module(tmp[0], tmp[1], tmp[2], tmp[3]);
                moduleList.add(newModule);
            }
            model.addAttribute("modules", moduleList);
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "setup/setupMain";
    }

    /**
     * Get Mapping for the creating a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/setupMain")
    @Secured("ROLE_setup")
    public String dashboard(final KeycloakAuthenticationToken token, final Model model) {
        List<String[]> modules = CSVService.getModulesWithDetails();
        List<Module> moduleList = new ArrayList<Module>();
        String[] tmp;
        for (int i = 0; i < modules.size(); i++) {
            tmp = modules.get(i);
            Module newModule = new Module(tmp[0], tmp[1], tmp[2], tmp[3]);
            moduleList.add(newModule);
        }
        model.addAttribute("modules", moduleList);
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "setup/setupMain";
    }

    /**
     * Get Mapping for the creating a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
 @GetMapping("/setupNewModule")
 @Secured("ROLE_setup")
    public String newModule(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "setup/setupNewModule";
    }
}