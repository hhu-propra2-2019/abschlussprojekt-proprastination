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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * kjgjhb
     * @param distributionId distributionId
     * @param applicantId applicantId
     * @param token token
     * @param model model
     * @return html
     */
    @PostMapping("/{distributionId}/{applicantId}/")
    @Secured("ROLE_verteiler")
    public String moved(@PathVariable("distributionId") final String distributionId,
                        @PathVariable("applicantId") final String applicantId,
                        final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            distributionService.move(applicantId, distributionId);
        }
        return "distributor/distributorMain";
    }
}
