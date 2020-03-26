package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Module;
import mops.services.ApplicantService;
import mops.services.ApplicationService;
import mops.services.DistributionService;
import mops.services.EMailService;
import mops.services.ModuleService;
import mops.services.PDFService;
import mops.services.ZIPService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/pdf")
public class PDFController {

    private ApplicantService applicantService;
    private PDFService pdfService;
    private ModuleService moduleService;
    private ApplicationService applicationService;
    private ZIPService zipService;
    private DistributionService distributionService;
    private EMailService eMailService;

    /**
     * Initiates PDF Controller
     *
     * @param applicantService applicantservice.
     * @param pdfService       pdfService.
     * @param moduleService    moduleService
     * @param applicationService applicationservice
     * @param zipService zipservice
     * @param distributionService distributionservice
     * @param eMailService emailservice
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public PDFController(final ApplicantService applicantService,
                         final PDFService pdfService,
                         final ModuleService moduleService,
                         final ApplicationService applicationService,
                         final ZIPService zipService,
                         final DistributionService distributionService,
                         final EMailService eMailService) {
        this.applicantService = applicantService;
        this.pdfService = pdfService;
        this.moduleService = moduleService;
        this.applicationService = applicationService;
        this.zipService = zipService;
        this.distributionService = distributionService;
        this.eMailService = eMailService;
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
     * dummy overview for pdf download
     * @param token token
     * @param model model
     * @return downloadhtml
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @GetMapping("/uebersicht")
    public String overviewPDFDownload(final KeycloakAuthenticationToken token, final Model model) {
        Account account = createAccountFromPrincipal(token);
        System.out.println(account.getRoles());
        model.addAttribute("account", account);
        List<Module> modules = moduleService.getModules();
        List<String> moduleNames = new ArrayList<>();
        for (Module module : modules) {
            moduleNames.add(module.getName());
        }
        model.addAttribute("modules", moduleNames);
        model.addAttribute("modulesStudent", moduleNames);
        return "pdfhandling";
    }

    /**
     * Choose applicant after chosen module and downlaod pdf
     * @param token token
     * @param model model
     * @param module module
     * @return applicant download pdf
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/downloadBewerber")
    public String downloadApplicant(final KeycloakAuthenticationToken token, final Model model,
                                   @RequestParam("modulesStudent") final String module) {

        Account account = createAccountFromPrincipal(token);
        model.addAttribute("account", account);

        Module mod = moduleService.findModuleByName(module);
        List<Application> applications = applicationService.findApplicationsByModule(mod);
        List<String> applicantUniserials = new ArrayList<>();
        for (Application application : applications) {
            applicantUniserials.add(applicantService.findByApplications(application).getUniserial());
        }
        model.addAttribute("module", module);
        model.addAttribute("applicants", applicantUniserials);

        return "pdfhandlingapplicant";
    }

    /**
     * download pdf with given module and applicant
     * @param token token
     * @param model model
     * @param module module
     * @param applicant applicant
     * @return download pdf
     */
    @PostMapping("/downloadBewerberFertig")
    @Secured({"ROLE_orga", "ROLE_verteiler"})
    public String downloadApplicantDone(final KeycloakAuthenticationToken token,
                                       final Model model,
                                       @RequestParam("module") final String module,
                                       @RequestParam("applicants") final String applicant) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
        }
        return "redirect:pdfDownload?student=" + applicant + "&module=" + module;
    }

    /**
     * download all applications for this module
     * @param token token
     * @param module module
     * @return module pdf
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/downloadModul")
    public String downloadModule(final KeycloakAuthenticationToken token,
                                  @RequestParam("modules") final String module) {
        return "redirect:zipModuleDownload?module=" + module;
    }

    /**
     * download all applications
     * @param token token
     * @return module
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/downloadAlles")
    public String downloadAll(final KeycloakAuthenticationToken token) {
        return "redirect:zipAllDownload";
    }

    /**
     * Views main page
     *
     * @param token keycloak
     * @param model model
     * @return html
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @GetMapping("/zuteilungUebersicht")
    public String distributerOverview(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            List<Distribution> distributions = distributionService.findAll();
            List<String> moduleNames = new ArrayList<>();
            for (Distribution distribution : distributions) {
                moduleNames.add(distribution.getModule().getName());
            }
            model.addAttribute("modules", moduleNames);
            model.addAttribute("modulesStudent", moduleNames);
        }
        return "pdfhandlingdistribution";
    }

    /**
     * Choose applicant after chosen module and downlaod pdf
     * @param token token
     * @param model model
     * @param module module
     * @return applicant download pdf
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/downloadBewerberZugeteilt")
    public String downloadApplicantDistributed(final KeycloakAuthenticationToken token, final Model model,
                                    @RequestParam("modulesStudent") final String module) {
        Account account = createAccountFromPrincipal(token);
        model.addAttribute("account", account);

        Module mod = moduleService.findModuleByName(module);
        Distribution distribution = distributionService.findByModule(mod);
        List<Applicant> applicants = distribution.getEmployees();
        List<String> applicantUniserials = new ArrayList<>();
        for (Applicant applicant : applicants) {
            applicantUniserials.add(applicant.getUniserial());
        }
        model.addAttribute("module", module);
        model.addAttribute("applicants", applicantUniserials);

        return "pdfhandlingapplicant";
    }

    /**
     * Redirect to download all
     *
     * @param token
     * @param model
     * @return download
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/downloadAllesZugeteilt")
    public String distributeDownloadAll(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
        }
        return "redirect:zipAllDistributedDownload";
    }

    /**
     *
     * @param token token
     * @param model model
     * @param eMail email
     * @return webpage
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/versenden")
    public String sendApplications(final KeycloakAuthenticationToken token,
                                   final Model model,
                                   @RequestParam("email") final String eMail) {
       if (token != null) {
           Account account = createAccountFromPrincipal(token);
           model.addAttribute("account", account);
           File file = zipService.getZipFileForAllDistributions();
           eMailService.sendEmailToRecipient(eMail, file);
       }
        return "redirect:zuteilungUebersicht";
    }
    /**
     * Returns a FileStream of the requested PDF.
     *
     * @param module  module name of the application.
     * @param student student uniserial.
     * @param token   Keycloak token.
     * @param model   Model.
     * @return InputStreamResource
     * @throws IOException NoSuchElementException
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "pdfDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> fileSystemResource(
            @RequestParam(value = "module") final String module, @RequestParam(value = "student") final String student,
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {

        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);


            HttpHeaders header = new HttpHeaders();

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

    /**
     * Downloads all applications for a module
     * @param module module
     * @param token token
     * @param model
     * @return file
     * @throws IOException
     * @throws NoSuchElementException
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "zipModuleDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> zipSystemResource(
            @RequestParam(value = "module") final String module,
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);

            HttpHeaders header = new HttpHeaders();

            if (module == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            List<Module> modules = new ArrayList<>();
            modules.add(moduleService.findModuleByName(module));
            File file = zipService.getZipFileForModule(modules);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download.zip\"");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Downloads all applications as zip
     * @param token token
     * @param model model
     * @return file
     * @throws IOException
     * @throws NoSuchElementException
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "zipAllDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> zipAllSystemResource(
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);

            HttpHeaders header = new HttpHeaders();

            List<Module> modules = moduleService.getModules();
            File file = zipService.getZipFileForModule(modules);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download.zip\"");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Downloads all distributed as zip
     * @param token token
     * @param model model
     * @return Zip file
     * @throws IOException
     * @throws NoSuchElementException
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "zipAllDistributedDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> zipAllDistributedSystemResource(
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);

            HttpHeaders header = new HttpHeaders();

            File file = zipService.getZipFileForAllDistributions();
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download.zip\"");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
