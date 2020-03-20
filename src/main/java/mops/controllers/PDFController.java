package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.services.ApplicantService;
import mops.services.PDFService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * @param token    Keycloak token.
     * @param model    Model.
     * @return InputStreamResource
     * @throws IOException NoSuchElementException
     */
    @Secured("ROLE_studentin")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ResponseEntity<Resource> fileSystemResource(
            @RequestParam(value = "module") final String module, final KeycloakAuthenticationToken token,
            final Model model) throws IOException, NoSuchElementException {

        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);


            HttpHeaders header = new HttpHeaders();

            Applicant applicant;
            Optional<Application> application;
            try {
                applicant = applicantService.findByUniserial(account.getName());
                application = applicant.getApplications().stream()
                        .filter(p -> p.getModule().equals(module)).findFirst();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            if (application.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            String filepath = pdfService.generatePDF(application.get(), applicant);
            File file = new File(filepath);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Bewerbung.pdf");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}
