package mops.controllers;

import mops.services.dbServices.DbDistributionService;
import mops.services.logicServices.DistributionService;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.AccountGenerator;
import mops.services.webServices.WebApplicationService;
import mops.services.webServices.WebPdfService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/pdf")
public class PDFController {

    private final ModuleService moduleService;
    private final WebApplicationService webApplicationService;
    private final DistributionService distributionService;
    private final DbDistributionService dbDistributionService;
    private final WebPdfService webPdfService;

    /**
     * Initiates PDF Controller
     * @param moduleService
     * @param webApplicationService
     * @param distributionService
     * @param dbDistributionService
     * @param webPdfService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public PDFController(final ModuleService moduleService,
                         final WebApplicationService webApplicationService,
                         final DistributionService distributionService,
                         final DbDistributionService dbDistributionService,
                         final WebPdfService webPdfService) {
        this.moduleService = moduleService;
        this.webApplicationService = webApplicationService;
        this.distributionService = distributionService;
        this.dbDistributionService = dbDistributionService;
        this.webPdfService = webPdfService;
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
        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("verteilt", distributionService.getSize());
        model.addAttribute("modules", moduleService.getModuleNames());
        model.addAttribute("modulesStudent", moduleService.getModuleNames());
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

        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("module", module);
        model.addAttribute("applicants", webApplicationService.getApplicantUniserialsByModule(module));

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
    public String downloadApplicantDone(final KeycloakAuthenticationToken token, final Model model,
                                       @RequestParam("module") final String module,
                                       @RequestParam("applicants") final String applicant) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        }
        return webPdfService.getDownloadRedirectOfApplicantWithModule(module, applicant);
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
        return webPdfService.getDownloadRedirectOfModule(module);
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
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            if (dbDistributionService.count() == 0) {
                return "redirect:uebersicht";
            }
            model.addAttribute("modules", moduleService.getModuleNames());
            model.addAttribute("modulesStudent", moduleService.getModuleNames());
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

        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("module", module);
        model.addAttribute("applicants", webApplicationService.getApplicantUniserialsByModule(module));

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
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        }
        return "redirect:zipAllDistributedDownload";
    }

    /**
     * @param token      token
     * @param attributes model
     * @param eMail      email
     * @return webpage
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @PostMapping("/versenden")
    public RedirectView sendApplications(final KeycloakAuthenticationToken token,
                                         final RedirectAttributes attributes,
                                         @RequestParam("email") final String eMail) {
        if (token != null) {
            webPdfService.sendEmail(token, attributes, eMail);
        }
        return new RedirectView("zuteilungUebersicht", true);
    }

    /**
     * Returns a FileStream of the requested PDF.
     *
     * @param module  module name of the application.
     * @param student student uniserial.
     * @param token   Keycloak token.
     * @param model   Model.
     * @return InputStreamResource
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "pdfDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> fileSystemResource(
            @RequestParam(value = "module") final String module, @RequestParam(value = "student") final String student,
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {

        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            return webPdfService.generatePdfForDownload(module, student);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Downloads all applications for a module
     *
     * @param module module
     * @param token  token
     * @param model model
     * @return file
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "zipModuleDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> zipSystemResource(
            @RequestParam(value = "module") final String module,
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            return webPdfService.generateSingleZip(module);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Downloads all applications as zip
     * @param token token
     * @param model model
     * @return file
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "zipAllDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> zipAllSystemResource(
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            return webPdfService.generateMultipleZip();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Downloads all distributed as zip
     * @param token token
     * @param model model
     * @return Zip file
     */
    @Secured({"ROLE_verteiler", "ROLE_orga"})
    @RequestMapping(value = "zipAllDistributedDownload", method = RequestMethod.GET)
    public ResponseEntity<Resource> zipAllDistributedSystemResource(
            final KeycloakAuthenticationToken token, final Model model) throws IOException, NoSuchElementException {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            return webPdfService.generateMultipleZipForAssigned();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
