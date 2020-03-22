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
    private static final int NAME = 0;
    private static final int SHORT_NAME = 1;
    private static final int PROF_NAME = 2;
    private static final int SEVEN_HOUR_LIMIT = 3;
    private static final int NINE_HOUR_LIMIT = 4;
    private static final int SEVENTEEN_HOUR_LIMIT = 5;
    private static final int HOUR_LIMIT = 6;

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
            List<Module> moduleList = new ArrayList<>();
            String[] tmp;
            for (String[] module : modules) {
                tmp = module;
                Module newModule = Module.builder()
                        .name(tmp[NAME])
                        .shortName(tmp[SHORT_NAME])
                        .profName(tmp[PROF_NAME])
                        .sevenHourLimit(tmp[SEVEN_HOUR_LIMIT])
                        .nineHourLimit(tmp[NINE_HOUR_LIMIT])
                        .seventeenHourLimit(tmp[SEVENTEEN_HOUR_LIMIT])
                        .hourLimit(tmp[HOUR_LIMIT])
                        .build();
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
     * @param module the module object with details
     * @return redirects to index
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    @PostMapping("/setupMain")
    @Secured("ROLE_setup")
    public String postEditedModule(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("oldName") final String oldName,
                                   final Module module) {
        CSVService.deleteModule(oldName);
        List<String[]> input = new ArrayList<>();
        String[] editedModule = module.toStringArray();
        input.add(editedModule);
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
            model.addAttribute("Module", Module.builder().build());
        }
        return "setup/neuesModul";
    }

    /**
     * Post mapping for saving a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param module wrapped Object with module details
     * @return redirects to index
     */
    @PostMapping("/neuesModul")
    @Secured("ROLE_setup")
    public String postNewModule(final KeycloakAuthenticationToken token, final Model model,
                                final Module module) {
        List<String[]> input = new ArrayList<>();
        String[] newModule = module.toStringArray();
        input.add(newModule);
        CSVService.writeInCSV("src/main/resources/csv/module.csv", input);
        return index(token, model);
    }

    /**
     * Post mapping for editing a module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param oldModule old module with details for pre filled text fields
     * @return redirects to index
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    @PostMapping("/modulBearbeiten")
    @Secured("ROLE_setup")
    public String postEditModule(final KeycloakAuthenticationToken token, final Model model,
                                 final Module oldModule) {
        model.addAttribute("module", oldModule);
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "/setup/modulBearbeiten";
    }

    /**
     * Post mapping for deleting a module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     * @param name module name
     */
    @PostMapping("/deleteModule")
    @Secured("ROLE_setup")
    public String postDeleteModule(final KeycloakAuthenticationToken token, final Model model,
                                @RequestParam("nameDelete") final String name) {
        CSVService.deleteModule(name);
        return index(token, model);
    }

    /**
     * Post mapping for deleting all modules
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return index Redirect to setupMain
     */
    @PostMapping("/alleModuleLoeschen")
    @Secured("ROLE_setup")
    public String postDeleteAllModule(final KeycloakAuthenticationToken token, final Model model) {
        CSVService.cleanModules();
        return index(token, model);
    }
}
