package mops.controllers;

import mops.model.Account;
import mops.model.classes.orgaWebClasses.WebListClass;
import mops.services.OrgaService;
import mops.services.OrganizerService;
import org.keycloak.KeycloakPrincipal;
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


@SessionScope
@Controller
@RequestMapping("/bewerbung2/organisator")
public class OrgaController {

    private final OrgaService orgaService;
    private final OrganizerService organizerService;

    /**
     * Lets Spring inject the services
     * @param organizerService organizerService
     * @param orgaService   orgaService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrgaController(final OrgaService orgaService,
                          final OrganizerService organizerService) {
        this.orgaService = orgaService;
        this.organizerService = organizerService;
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
    public String index(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("modules", organizerService.getOrganizerModulesByName(token.getName()));
            model.addAttribute("organizer", organizerService.getOrganizerOrNewOrganizer(token.getName()));
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
            model.addAttribute("account", createAccountFromPrincipal(token));
            if (!token.getName().equals(moduleService.findById(Long.parseLong(id)).getProfSerial())) {
                return "redirect:/bewerbung2/organisator/";
            }
                List<WebList> applications = orgaService.getAllListEntrys(id);
                WebListClass webListClass = new WebListClass(applications);
                model.addAttribute("WebList", webListClass);
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
            model.addAttribute("account", createAccountFromPrincipal(token));
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
            organizerService.changePhonenumber(token.getName(), phone);
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
