package mops.controllers;

import mops.model.classes.orgaWebClasses.WebListClass;
import mops.services.webServices.OrgaService;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.AccountGenerator;
import mops.services.webServices.WebOrganizerService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
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

import java.time.LocalDateTime;


@SessionScope
@Controller
@RequestMapping("/bewerbung2/organisator")
public class OrgaController {

    private final OrgaService orgaService;
    private final WebOrganizerService webOrganizerService;
    private final ModuleService moduleService;

    /**
     * Lets Spring inject the services
     * @param orgaService   orgaService
     * @param webOrganizerService webOrganizerService
     * @param moduleService moduleservice
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrgaController(final OrgaService orgaService, final WebOrganizerService webOrganizerService,
                          final ModuleService moduleService) {
        this.orgaService = orgaService;
        this.webOrganizerService = webOrganizerService;
        this.moduleService = moduleService;
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
    public String index(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            model.addAttribute("modules", webOrganizerService.getOrganizerModulesByName(token.getName()));
            model.addAttribute("organizer", webOrganizerService.getOrganizerOrNewOrganizer(token.getName(),
                    AccountGenerator.createAccountFromPrincipal(token), token));
        }

        return "organizer/orgaMain";
    }

    /**
     * Shows overview of applications for a module.
     *
     * @param id    the applications is, as Path variable
     * @param token Keycloak token
     * @param model Model.
     * @return orgaOverview.html as String
     */
    @GetMapping("/{id}/")
    @Secured("ROLE_orga")
    public String overview(@PathVariable("id") final String id, final KeycloakAuthenticationToken token,
                           final Model model) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            if (!token.getName().equals(moduleService.findById(Long.parseLong(id)).getProfSerial())
            || !webOrganizerService.checkForPhoneNumber(token.getName())
            || moduleService.findById(Long.parseLong(id)).getOrgaDeadline().isBefore(LocalDateTime.now())
            || LocalDateTime.now().isBefore(moduleService.findById(Long.parseLong(id)).getApplicantDeadline())) {
                return "redirect:/bewerbung2/organisator/";
            }
            model.addAttribute("WebList", new WebListClass(orgaService.getAllListEntrys(id)));
        }
        return "organizer/orgaOverview";
    }

    /**
     * @param token token
     * @param applications WebListClass applications
     * @param id           module id
     * @param model        model
     * @return redirect orgaOverview
     */
    @PostMapping("/{id}/")
    @Secured("ROLE_orga")
    public String applicationInfoPost(final KeycloakAuthenticationToken token,
                                      @ModelAttribute final WebListClass applications,
                                      @PathVariable("id") final String id, final Model model) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            orgaService.saveEvaluations(applications);
        }
        return "redirect:/bewerbung2/organisator/" + id + "/";
    }

    /**
     * Post mapping for saving edited phone number
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param phone The new phone number
     * @return redirects to orgaMain
     */
    @PostMapping("/")
    @Secured("ROLE_orga")
    public String postEditedModule(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("phone") final String phone) {
        if (token != null) {
            webOrganizerService.changePhonenumber(token.getName(), phone);
        }
        return "redirect:/bewerbung2/organisator/";
    }


    /**
     * Needed to display additional information about each application on the overview page.
     * (Inside a modal / popup window.)
     * @param id applications id
     * @param model Model
     * @return "applicationModalContent", the HTML file with the modal content.
     */
    @GetMapping("/modal/{id}/")
    @Secured("ROLE_orga")
    public String applicationInfoGet(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("appl", orgaService.getApplication(id));
        return "organizer/applicationModalContent";
    }

}
