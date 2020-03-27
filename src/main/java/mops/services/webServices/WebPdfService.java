package mops.services.webServices;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.services.EMailService;
import mops.services.PDFService;
import mops.services.ZIPService;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WebPdfService {

    private final ModuleService moduleService;
    private final EMailService eMailService;
    private final ZIPService zipService;
    private final ApplicantService applicantService;
    private final PDFService pdfService;

    /**
     * Constructor
     * @param eMailService
     * @param zipService
     * @param applicantService
     * @param pdfService
     * @param moduleService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebPdfService(final EMailService eMailService, final ZIPService zipService,
                         final ApplicantService applicantService, final PDFService pdfService,
                         final ModuleService moduleService) {
        this.eMailService = eMailService;
        this.zipService = zipService;
        this.applicantService = applicantService;
        this.pdfService = pdfService;
        this.moduleService = moduleService;
    }

    /**
     * -
     * @param module
     * @param applicant
     * @return -
     */
    public String getDownloadRedirectOfApplicantWithModule(final String module, final String applicant) {
        return "redirect:pdfDownload?student=" + applicant + "&module=" + module;
    }

    /**
     * -
     * @param module
     * @return -
     */
    public String getDownloadRedirectOfModule(final String module) {
        return "redirect:zipModuleDownload?module=" + module;
    }

    /**
     * -
     * @param attachment
     * @return -
     */
    public HttpHeaders generateHttpHeader(final String attachment) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, attachment);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    /**
     * -
     * @param header
     * @param file
     * @param resource
     * @param applicationOctetStream
     * @return -
     */
    public ResponseEntity<Resource> generateResponseEntity(final HttpHeaders header,
                                                           final File file,
                                                           final ByteArrayResource resource,
                                                           final MediaType applicationOctetStream) {
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(applicationOctetStream)
                .body(resource);
    }

    /**
     * -
     * @param token
     * @param attributes
     * @param eMail
     */
    public void sendEmail(final KeycloakAuthenticationToken token,
                          final RedirectAttributes attributes, final String eMail) {
        attributes.addFlashAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        String message;
        String type;
        try {
            File file = zipService.getZipFileForAllDistributions();
            eMailService.sendEmailToRecipient(eMail, file);
            message = "Die Email wurde erfolgreich versendet";
            type = "success";
        } catch (MailSendException | IOException e) {
            message = e.getMessage();
            type = "danger";
        }
        attributes.addFlashAttribute("message", message);
        attributes.addFlashAttribute("type", type);
    }

    /**
     * -
     * @param module
     * @param student
     * @return -
     * @throws IOException
     */
    public ResponseEntity<Resource> generatePdfForDownload(final String module,
                                                           final String student) throws IOException {
        Applicant applicant;
        Optional<Application> application;
        try {
            if (module == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            applicant = applicantService.findByUniserial(student);
            application = applicant.getApplications().stream()
                    .filter(p -> p.getModule().getName().equals(module)).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (application.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        File file = pdfService.generatePDF(application.get(), applicant);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = generateHttpHeader("attachment; filename=Bewerbung.pdf");

        return generateResponseEntity(header, file, resource, MediaType.APPLICATION_PDF);
    }

    /**
     * -
     * @param module
     * @return -
     * @throws IOException
     */
    public ResponseEntity<Resource> generateSingleZip(final String module) throws IOException {
        if (module == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Module> modules = new ArrayList<>();
        modules.add(moduleService.findModuleByName(module));
        File file = zipService.getZipFileForModule(modules);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = generateHttpHeader("attachment; filename=\"download.zip\"");

        return generateResponseEntity(header, file, resource, MediaType.APPLICATION_OCTET_STREAM);
    }

    /**
     * -
     * @return -
     * @throws IOException
     */
    public ResponseEntity<Resource> generateMultipleZip() throws IOException {
        List<Module> modules = moduleService.getModules();
        File file = zipService.getZipFileForModule(modules);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = generateHttpHeader("attachment; filename=\"download.zip\"");

        return generateResponseEntity(header, file, resource, MediaType.APPLICATION_OCTET_STREAM);
    }

    /**
     * -
     * @return -
     * @throws IOException
     */
    public ResponseEntity<Resource> generateMultipleZipForAssigned() throws IOException {
        File file = zipService.getZipFileForAllDistributions();
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = generateHttpHeader("attachment; filename=\"download.zip\"");

        return generateResponseEntity(header, file, resource, MediaType.APPLICATION_OCTET_STREAM);
    }

}
