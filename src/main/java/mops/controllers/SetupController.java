package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.dbServices.DeletionService;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.AccountGenerator;
import mops.services.webServices.WebModuleService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/setup")
public class SetupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetupController.class);
    private final WebModuleService webModuleService;

    private final ApplicantService applicantService;

    private final ModuleService moduleService;

    private final DeletionService deletionService;

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
        this.webModuleService = webService;
        this.applicantService = applicantService;
        this.moduleService = moduleService;
        this.deletionService = deletionService;
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
            List<WebModule> modules = webModuleService.getModules();
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
     * @param bindingResult the result
     * @return redirects to index
     */
    @PostMapping("/setupMain")
    @Secured("ROLE_setup")
    public String postEditedModule(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("oldName") final String oldName,
                                   @Valid final WebModule module, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> LOGGER.info("ERROR {}", err.getDefaultMessage()));
            model.addAttribute("oldName", oldName);
            model.addAttribute("module", module);
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            return "/setup/modulBearbeiten";
        }
        webModuleService.update(module, oldName);
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
     * @param bindingResult the result of validating module
     * @return redirects to index
     */
    @PostMapping("/neuesModul")
    @Secured("ROLE_setup")
    public String postNewModule(final KeycloakAuthenticationToken token, final Model model,
                                @Valid final WebModule module, final BindingResult bindingResult) {
        LocalDateTime applicantDeadline = LocalDateTime.parse(module.getApplicantDeadlineDate()
                + "T" + module.getApplicantDeadlineTime());
        LocalDateTime orgaDeadline = LocalDateTime.parse(module.getOrgaDeadlineDate()
                + "T" + module.getOrgaDeadlineTime());
        if (applicantDeadline.isAfter(orgaDeadline)) {
            bindingResult.addError(new ObjectError("module",
                    "Die Bearbeitungsfrist darf nicht vor der Bewerbungsfrist sein."));
        }
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> LOGGER.info("ERROR {}", err.getDefaultMessage()));
            if (token != null) {
                model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
                model.addAttribute("Module", WebModule.builder().build());
            }
            return "setup/neuesModul";
        }

        webModuleService.save(module);
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
        model.addAttribute("oldName", oldModule.getName());
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
        webModuleService.deleteOne(name);
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
        webModuleService.deleteAll();
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
            Account account = AccountGenerator.createAccountFromPrincipal(token);
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
            Account account = AccountGenerator.createAccountFromPrincipal(token);
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
            Account account = AccountGenerator.createAccountFromPrincipal(token);
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
            Account account = AccountGenerator.createAccountFromPrincipal(token);
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
            Account account = AccountGenerator.createAccountFromPrincipal(token);
            attributes.addFlashAttribute("message", deletionService.deleteApplication(application, account));
        }
        return new RedirectView("loeschen", true);
    }


}
