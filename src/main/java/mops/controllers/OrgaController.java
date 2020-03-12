package mops.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import mops.db.dto.ApplicantDTO;
import mops.model.Account;
import mops.model.classes.Applicant;
import mops.services.ApplicantService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import java.awt.desktop.AppReopenedListener;
import java.sql.SQLException;

@SessionScope
@Controller
@RequestMapping("/bewerbung2/organisator")
public class OrgaController {

    @Autowired
    private ApplicantService applicantService;

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
        Applicant applicant = Applicant.builder().name("Rot").birthday("01.01.2020").course("ProPra").build();

        ObjectMapper mapper = new ObjectMapper();
        ApplicantDTO dto = new ApplicantDTO();
        dto.setUsername("Tom");
        dto.setDetails(mapper.writeValueAsString(applicant));
        applicantService.save(dto);
        System.out.println(dto.getUsername());

        //Iterable<ApplicantDTO> iter = applicantService.getAllIterable();
        System.out.println(applicantService.getAll());


        Applicant applicant1 = applicantService.findByUsername(dto.getUsername());
        System.out.println(applicant1);
        applicantService.save(applicant1, dto.getUsername());
        applicantService.save(applicant1, null);
        dto = applicantService.find(dto.getUsername());

        /*for (ApplicantDTO s : iter) {
            System.out.println(s);
        }*/


        return "orgaMain";
    }



}
