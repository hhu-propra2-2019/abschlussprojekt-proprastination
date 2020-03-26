package mops.controllers;

import mops.model.Account;
import mops.model.classes.Module;
import mops.model.classes.orgaWebClasses.WebList;
import mops.model.classes.orgaWebClasses.WebListClass;
import mops.services.ModuleService;
import mops.services.OrgaService;
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
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@SessionScope
@Controller
@RequestMapping("/bewerbung2/organisator")
public class OrgaController {

    private final ModuleService moduleService;
    private final OrgaService orgaService;

    /**
     * Lets Spring inject the services
     *
     * @param moduleService moduleService
     * @param orgaService   orgaService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrgaController(final ModuleService moduleService, final OrgaService orgaService) {
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
    public String index(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            List<Module> modules = new ArrayList<>();
            for (Module module : moduleService.getModules()) {
                if (module.getProfName().equals(token.getName())) {
                    modules.add(module);
                }
            }
            model.addAttribute("modules", modules);
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
        }
        List<WebList> applications = orgaService.getAllListEntrys(id);
        WebListClass webListClass = new WebListClass(applications);
        model.addAttribute("WebList", webListClass);
        return "organizer/orgaOverview";
    }

    /**
     * @param applications WebListClass applications
     * @param id           module id
     * @param model        model
     * @return redirect orgaOverview
     */
    @PostMapping("/{id}/")
    @Secured("ROLE_orga")
    public String applicationInfoPost(@ModelAttribute final WebListClass applications,
                                      @PathVariable("id") final String id, final Model model) {
        orgaService.saveEvaluations(applications);
        return "redirect:/bewerbung2/organisator/" + id + "/";
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
