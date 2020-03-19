package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.services.ApplicantService;
import mops.services.PDFService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/bewerber/pdf")
public class PDFController {

    private ApplicantService applicantService;

    private PDFService pdfService;

    /**
     * Initiates PDF Controller
     *
     * @param applicantService applicantservice.
     * @param pdfService       pdfService.
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public PDFController(final ApplicantService applicantService, final PDFService pdfService) {
        this.applicantService = applicantService;
        this.pdfService = pdfService;
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
     * Returns a FileStream of the requested PDF.
     *
     * @param module   module name of the application.
     * @param response http response.
     * @param token    Keycloak token.
     * @param model    Model.
     * @return InputStreamResource
     * @throws IOException NoSuchElementException
     */
    @Secured("ROLE_studentin")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public InputStreamResource fileSystemResource(
            @RequestParam(value = "module") final String module,
            final HttpServletResponse response, final KeycloakAuthenticationToken token,
            final Model model) throws IOException, NoSuchElementException {
        InputStreamResource resource = null;
        String filepath;
        if (token != null) {
            Account account = createAccountFromPrincipal(token);

            Applicant applicant = applicantService.findByUniserial(account.getName());
            Optional<Application> application = applicant.getApplications().stream()
                    .filter(p -> p.getModule().equals(module)).findFirst();

            if (application.isPresent()) {
                filepath = pdfService.generatePDF(application.get(), applicant);

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "Ihre Bewerbung");
                resource = new InputStreamResource(new FileInputStream(filepath));
            } else {
                throw new NoSuchElementException("No such application!");
            }
        }
        return resource;
    }

}
