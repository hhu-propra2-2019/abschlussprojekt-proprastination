package mops.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import mops.model.Account;
import mops.model.classes.orgaWebClasses.OrgaApplication;
import mops.services.ApplicantService;
import mops.services.ApplicationService;
import mops.services.ModuleService;
import mops.services.OrgaService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedList;
import java.util.List;

@SessionScope
@Controller
@RequestMapping("/bewerbung2/organisator")
public class OrgaController {

    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private final ModuleService moduleService;
    private final OrgaService orgaService;

    /**
     * Lets Spring inject the services
     * @param applicantService
     * @param applicationService
     * @param moduleService
     * @param orgaService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrgaController(final ApplicantService applicantService, final ApplicationService applicationService,
                          final ModuleService moduleService, final OrgaService orgaService) {
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.moduleService = moduleService;
        this.orgaService = orgaService;
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
        model.addAttribute("modules", moduleService.getModules());
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
     * @param id the applications is, as Path variable
     * @param token
     * @param model
     * @return orgaOverview.html as String
     */
    @GetMapping("/{id}/")
    @Secured("ROLE_orga")
    public String overview(@PathVariable("id") final String id, final KeycloakAuthenticationToken token,
                           final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        model.addAttribute("applications", orgaService.getAllApplications(id));
        return "organizer/orgaOverview";
    }

    /**
     *
     * @param applications
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/{id}/")
    @Secured("ROLE_orga")
    public String applicationInfoPost(final LinkedList<OrgaApplication> applications,
                                      @PathVariable("id") final String id, final Model model) {
        System.out.println("---------------------------------------------------------------------");
        System.out.println(applications.size());
        applications.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------");
        return "redirect:/bewerbung2/organisator/" + id + "/";
    }

    /**
     * Needed to display additional information about each application on the overview page.
     * (Inside a modal / popup window.)
     * @param id applications id
     * @param model
     * @return "applicationModalContent", the HTML file with the modal content.
     */
    @GetMapping("/modal/{id}/")
    @Secured("ROLE_orga")
    public String applicationInfoGet(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("appl", orgaService.getApplication(id));
        return "organizer/applicationModalContent";
    }

}
