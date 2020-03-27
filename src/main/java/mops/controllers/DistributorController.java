package mops.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import mops.model.Account;
import mops.model.classes.webclasses.WebDistribution;
import mops.services.DistributionService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;


@SessionScope
@Controller
@RequestMapping("/bewerbung2/verteiler")
public class DistributorController {

    private final DistributionService distributionService;

    /**
     * Constructor
     * @param distributionService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DistributorController(final DistributionService distributionService) {
        this.distributionService = distributionService;
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
    @Secured("ROLE_verteiler")
    public String index1(final KeycloakAuthenticationToken token, final Model model) throws JsonProcessingException {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            List<WebDistribution> webDistributionList = distributionService.convertDistributionsToWebDistributions();
            model.addAttribute("distributions", webDistributionList);
        }
        return "distributor/distributorMain";
    }

    /**
     * Maps when an Applicant is moved
     * @param distributionId distributionId
     * @param applicantId applicantId
     * @param token token
     * @param model model
     * @return redirect on mainpage as String
     */
    @GetMapping("/move/")
    @Secured("ROLE_verteiler")
    public void moved(@RequestParam("distributionId") final String distributionId,
                        @RequestParam("applicantId") final String applicantId,
                        final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            distributionService.moveApplicant(applicantId, distributionId);
        }
    }

    /**
     * saves the data the distributor changed
     * @param applicantId applicantId
     * @param distributionId distributionId
     * @param hours hours
     * @param token token
     * @param model model
     * @return redirect on mainpage as String
     */
    @GetMapping("/saveHours/")
    @Secured("ROLE_verteiler")
    public void saveHours(@RequestParam("applicantId") final String applicantId,
                            @RequestParam("distributionId") final String distributionId,
                            @RequestParam("hours") final String hours,
                            final KeycloakAuthenticationToken token,
                            final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            distributionService.saveHours(applicantId, distributionId, hours);
        }
    }

    /**
     * saves the data the distributor changed
     * @param applicantId applicantId
     * @param checked checked
     * @param token token
     * @param model model
     * @return redirect on mainpage as String
     */
    @GetMapping("/saveChecked/")
    @Secured("ROLE_verteiler")
    public void saveChecked(
            @RequestParam("applicantId") final String applicantId,
            @RequestParam("checked") final String checked,
            final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            distributionService.saveChecked(applicantId, checked);
        }
    }

    /**
     * Calls Distribution function
     * @param token token
     * @param model model
     * @return redirect on mainpage as String
     */
    @GetMapping("/vorverteilen")
    @Secured("ROLE_verteiler")
    public String distribute(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            distributionService.changeAllFinalHours();
            distributionService.distribute();
        }
        return "redirect:/bewerbung2/verteiler/";
    }
}
