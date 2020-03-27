package mops.controllers;

import mops.model.Account;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.WebModuleService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.Valid;
import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/setup")
public class SetupController {
    @Autowired
    private WebModuleService webService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

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
            List<WebModule> modules = webService.getModules();
            model.addAttribute("modules", modules);
            model.addAttribute("account", createAccountFromPrincipal(token));
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
     * @param bindingResult the result
     * @return redirects to index
     */
    @PostMapping("/setupMain")
    @Secured("ROLE_setup")
    public String postEditedModule(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("oldName") final String oldName,
                                   @Valid final WebModule module, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
            model.addAttribute("module", module);
            model.addAttribute("account", createAccountFromPrincipal(token));
            return "/setup/modulBearbeiten";
        }
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
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("Module", WebModule.builder().build());
        }
        return "setup/neuesModul";
    }

    /**
     * Post mapping for saving a new module
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param module wrapped Object with module details
     * @param bindingResult the result of validating module
     * @return redirects to index
     */
    @PostMapping("/neuesModul")
    @Secured("ROLE_setup")
    public String postNewModule(final KeycloakAuthenticationToken token, final Model model,
                                @Valid final WebModule module, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
            if (token != null) {
                model.addAttribute("account", createAccountFromPrincipal(token));
                model.addAttribute("Module", WebModule.builder().build());
            }
            return "setup/neuesModul";
        }
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
        model.addAttribute("account", createAccountFromPrincipal(token));
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
