package mops.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.services.ApplicantService;
import mops.services.DistributionService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@SessionScope
@Controller
@RequestMapping("/bewerbung2/verteiler")
public class DistributorController {

    @Autowired
    private ApplicantService applicantService;
    private DistributionService distributionService;

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

    @SuppressWarnings("checkstyle:MagicNumber")
    @GetMapping("/")
    @Secured("ROLE_verteiler")
    public String index(final KeycloakAuthenticationToken token, final Model model) throws JsonProcessingException {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            List<WebDistribution> webDistributionList = new ArrayList<>();
            List<Distribution> distributionList = new ArrayList<>();
            for (Distribution distribution : distributionList) {
                List<WebDistributorApplicant> webDistributorApplicantList = new ArrayList<>();
                List<Applicant> applicantList = new ArrayList<>();
                //TODO Applicant wieder austauschen
                for (Applicant applicant : applicantList) {
                    Application application = Application.builder()
                            .applicant(applicant)
                            .comment("ich bin sehr gut")
                            .grade(1.0)
                            .hours(17)
                            .id(1)
                            .lecturer("prof")
                            .module(distribution.getModule())
                            .priority(2)
                            .role("Korrektor")
                            .semester("SS19")
                            .build();
                    //Application application = applicantService.findApplicationByUsernameAndModule(
                    //        applicantList.get(j).getUniserial(),
                    //        distributionList.get(i).getModule());
                    //Evaluation evaluation = evaluationService.findByApplication(application);
                    //TODO folgende Evaluation wieder rausnehmen
                    Evaluation evaluation = Evaluation.builder()
                            .application(application)
                            .comment("blablabla")
                            .id(1)
                            .priority(1)
                            .build();
                    WebDistributorApplicant webDistributorApplicant = WebDistributorApplicant.builder()
                            .username(applicant.getUniserial())
                            .applicantPriority(application.getPriority() + "")
                            .minHours(application.getHours() + "")
                            .maxHours(application.getHours() + "")
                            .organizerPriority(evaluation.getPriority() + "")
                            //.organizerHours(evaluation.getHours + "")
                            .build();
                    webDistributorApplicantList.add(webDistributorApplicant);
                }
                WebDistribution webDistribution = WebDistribution.builder()
                        .module(distribution.getModule())
                        .webDistributorApplicants(webDistributorApplicantList)
                        .build();
                webDistributionList.add(webDistribution);
            }
            model.addAttribute(webDistributionList);
        }
        return "distributor/distributorMain";
    }
}
