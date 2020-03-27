package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.ApplicantService;
import mops.services.DeletionService;
import mops.services.ModuleService;
import mops.services.WebModuleService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/setup")
public class SetupController {
    private WebModuleService webService;

    private ApplicantService applicantService;

    private ModuleService moduleService;

    private DeletionService deletionService;

    /**
     * Constructor
     *
     * @param webService       webservice
     * @param applicantService applicantservice
     * @param moduleService    moduleservice
     * @param deletionService  deletionservice
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public SetupController(final WebModuleService webService, final ApplicantService applicantService,
                           final ModuleService moduleService, final DeletionService deletionService) {
        this.webService = webService;
        this.applicantService = applicantService;
        this.moduleService = moduleService;
        this.deletionService = deletionService;
    }

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

    /**
     * Deletion Mainpage
     *
     * @param token Keycloak token
     * @param model Model
     * @return returns deletion Mainpage
     */
    @GetMapping("/loeschen")
    @Secured("ROLE_setup")
    public String deleteMain(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            List<Module> modules = moduleService.getModules();
            List<Applicant> applicants = applicantService.findAll();
            model.addAttribute("account", account);
            model.addAttribute("modules", modules);
            model.addAttribute("applicants", applicants);
        }
        return "setup/deleteMain";
    }

    /**
     * Mapping to delete everything.
     *
     * @param token      keycloaktoken
     * @param attributes redirect attribute.
     * @return redirect to deletion mainpage
     */
    @GetMapping("/loescheAlles")
    @Secured("ROLE_setup")
    public RedirectView deleteAll(final KeycloakAuthenticationToken token, final RedirectAttributes attributes) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            attributes.addFlashAttribute("message", deletionService.deleteAll(account));
        }
        return new RedirectView("loeschen", true);
    }

    /**
     * Mapping to delete Module
     *
     * @param module     modulename
     * @param token      keycloaktoken
     * @param attributes redirect attribute
     * @return redirect to mainpage
     */
    @PostMapping("/loescheModul")
    @Secured("ROLE_setup")
    public RedirectView deleteModule(@ModelAttribute("module") final String module,
                                     final KeycloakAuthenticationToken token, final RedirectAttributes attributes) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            attributes.addFlashAttribute("message", deletionService.deleteModule(module, account));
        }
        return new RedirectView("loeschen", true);
    }

    /**
     * Mapping to delete Applicant
     *
     * @param applicant  Applicant uniserial
     * @param token      keycloaktoken
     * @param attributes redirect attributes
     * @return redirect to mainpage
     */
    @PostMapping("/loescheApplicant")
    @Secured("ROLE_setup")
    public RedirectView deleteApplicant(@ModelAttribute("applicant") final String applicant,
                                        final KeycloakAuthenticationToken token, final RedirectAttributes attributes) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            attributes.addFlashAttribute("message", deletionService.deleteApplicant(applicant, account));
        }
        return new RedirectView("loeschen", true);
    }

    /**
     * Mapping for deletion of application
     *
     * @param application application id
     * @param token       keycloak token
     * @param attributes  redirect attributes
     * @return redirects to deletion mainpage
     */
    @PostMapping("/loescheApplication")
    @Secured("ROLE_setup")
    public RedirectView deleteApplication(@ModelAttribute("application") final long application,
                                          final KeycloakAuthenticationToken token,
                                          final RedirectAttributes attributes) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            attributes.addFlashAttribute("message", deletionService.deleteApplication(application, account));
        }
        return new RedirectView("loeschen", true);
    }


}
