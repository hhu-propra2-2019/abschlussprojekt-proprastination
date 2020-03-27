package mops.controllers;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.webServices.AccountGenerator;
import mops.services.webServices.WebModuleService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/setup")
public class SetupController {

    private final WebModuleService webService;

    /**
     * Constructor
     * @param webService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public SetupController(final WebModuleService webService) {
        this.webService = webService;
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
            List<WebModule> modules = webService.getModules();
            model.addAttribute("modules", modules);
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            model.addAttribute("module", Module.builder().build());
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
    @PostMapping("/setupMain")
    @Secured("ROLE_setup")
    public String postEditedModule(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("oldName") final String oldName,
                                   final WebModule module) {
        webService.update(module, oldName);
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
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            model.addAttribute("Module", WebModule.builder().build());
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
                                final WebModule module) {
        webService.save(module);
        return index(token, model);
    }

    /**
     * Post mapping for editing a module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param oldModule old module with details for pre filled text fields
     * @return redirects to index
     */
    @PostMapping("/modulBearbeiten")
    @Secured("ROLE_setup")
    public String postEditModule(final KeycloakAuthenticationToken token, final Model model,
                                 final WebModule oldModule) {
        model.addAttribute("module", oldModule);
        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        return "/setup/modulBearbeiten";
    }

    /**
     * Post mapping for deleting a module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param name module name
     * @return The HTML file rendered as a String
     */
    @PostMapping("/deleteModule")
    @Secured("ROLE_setup")
    public String postDeleteModule(final KeycloakAuthenticationToken token, final Model model,
                                @RequestParam("nameDelete") final String name) {
        webService.deleteOne(name);
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
        webService.deleteAll();
        return index(token, model);
    }
}
