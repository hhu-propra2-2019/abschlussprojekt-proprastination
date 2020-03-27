package mops.services.webServices;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebApplication;
import mops.services.EMailService;
import mops.services.PDFService;
import mops.services.ZIPService;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    public WebPdfService(EMailService eMailService, ZIPService zipService, ApplicantService applicantService, PDFService pdfService, ModuleService moduleService) {
        this.eMailService = eMailService;
        this.zipService = zipService;
        this.applicantService = applicantService;
        this.pdfService = pdfService;
        this.moduleService = moduleService;
    }

    public String getDownloadRedirectOfApplicantWithModule(String module, String applicant) {
        return "redirect:pdfDownload?student=" + applicant + "&module=" + module;
    }

    public String getDownloadRedirectOfModule(String module) {
        return "redirect:zipModuleDownload?module=" + module;
    }

    public HttpHeaders generateHttpHeader(String attachment) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, attachment);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    public ResponseEntity<Resource> generateResponseEntity(HttpHeaders header, File file, ByteArrayResource resource, MediaType applicationOctetStream) {
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(applicationOctetStream)
                .body(resource);
    }

    public void sendEmail(KeycloakAuthenticationToken token, RedirectAttributes attributes, @RequestParam("email") String eMail) {
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

    public ResponseEntity<Resource> generatePdfForDownload(String module, String student) throws IOException {
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

    public ResponseEntity<Resource> generateSingleZip(String module) throws IOException {
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

    public ResponseEntity<Resource> generateMultipleZip() throws IOException {
        List<Module> modules = moduleService.getModules();
        File file = zipService.getZipFileForModule(modules);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = generateHttpHeader("attachment; filename=\"download.zip\"");

        return generateResponseEntity(header, file, resource, MediaType.APPLICATION_OCTET_STREAM);
    }

    public ResponseEntity<Resource> generateMultipleZipForAssigned() throws IOException {
        File file = zipService.getZipFileForAllDistributions();
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = generateHttpHeader("attachment; filename=\"download.zip\"");

        return generateResponseEntity(header, file, resource, MediaType.APPLICATION_OCTET_STREAM);
    }

}
