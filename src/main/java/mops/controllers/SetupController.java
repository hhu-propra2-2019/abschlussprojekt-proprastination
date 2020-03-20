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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                Module newModule = new Module(tmp[0], tmp[1], tmp[2], tmp[3], tmp[4]);
                moduleList.add(newModule);
            }
            model.addAttribute("modules", moduleList);
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "setup/setupMain";
    }

    /**
     * Post mapping for saving an edited module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param oldName module name
     * @param name module name
     * @param shortName module short name
     * @param prof responsible person
     * @param hourLimit maximum hours of work required
     * @param personLimit maximum people of work required
     * @return redirects to index
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    @PostMapping("/setupMain")
    public String postEditedModule(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("oldName") final String oldName,
                                   @RequestParam("name") final String name,
                                   @RequestParam("shortName") final String shortName,
                                   @RequestParam("profName") final String prof,
                                   @RequestParam("hourLimit") final String hourLimit,
                                   @RequestParam("personLimit") final String personLimit) {
        CSVService.deleteModule(oldName);
        List<String[]> input = new ArrayList<>();
        String[] s1 = {name, shortName, prof, hourLimit, personLimit};
        input.add(s1);
        CSVService.writeInCSV("src/main/resources/csv/module.csv", input);
        return index(token, model);
    }

    /**
     * Get Mapping for the creating a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/neuesModul")
    @Secured("ROLE_setup")
    public String newModule(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "setup/neuesModul";
    }

    /**
     * Post mapping for saving a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     * @param name module name
     * @param shortName module short name
     * @param prof responsible person
     * @param hourLimit maximum hours of work required
     * @param personLimit maximum people of work required
     */
    @PostMapping("/neuesModul")
    public String postNewModule(final KeycloakAuthenticationToken token, final Model model,
                                @RequestParam("name") final String name,
                                @RequestParam("shortName") final String shortName,
                                @RequestParam("prof") final String prof,
                                @RequestParam("hourLimit") final String hourLimit,
                                @RequestParam("personLimit") final String personLimit) {
        List<String[]> input = new ArrayList<>();
        String[] s1 = {name, shortName, prof, hourLimit, personLimit};
        input.add(s1);
        CSVService.writeInCSV("src/main/resources/csv/module.csv", input);
        return index(token, model);
    }

    /**
     * Postmapping for editing the selected module
     * @param token
     * @param model
     * @param name
     * @param shortName
     * @param prof
     * @param hourLimit
     * @param personLimit
     * @return setup/modulBearbeiten
     */
    @PostMapping("/modulBearbeiten")
    public String postEditModule(final KeycloakAuthenticationToken token, final Model model,
                                 @RequestParam("name") final String name,
                                 @RequestParam("shortName") final String shortName,
                                 @RequestParam("prof") final String prof,
                                 @RequestParam("hourLimit") final String hourLimit,
                                 @RequestParam("personLimit") final String personLimit) {
        Module oldModul = new Module(name, shortName, prof, hourLimit, personLimit);
        model.addAttribute("module", oldModul);
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "/setup/modulBearbeiten";
    }

    /**
     * Post mapping for saving a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     * @param name module name
     */
    @PostMapping("/deleteModule")
    public String postDeleteModule(final KeycloakAuthenticationToken token, final Model model,
                                @RequestParam("nameDelete") final String name) {
        CSVService.deleteModule(name);
        return index(token, model);
    }
}
