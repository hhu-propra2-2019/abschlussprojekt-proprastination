package mops.controllers;

import mops.model.classes.webclasses.DownloadProgress;
import mops.services.dbServices.DbDistributionService;
import mops.services.logicServices.DistributionService;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.AccountGenerator;
import mops.services.webServices.WebApplicationService;
import mops.services.webServices.WebPdfService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
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
    private DownloadProgress downloadProgress;

    /**
     * Initiates PDF Controller
     *
     * @param moduleService         Module Service
     * @param webApplicationService Application Service
     * @param distributionService   Distribution Service
     * @param dbDistributionService Database Distribution Service
     * @param webPdfService         Web PDF Service
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
        this.downloadProgress = new DownloadProgress();
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
    @Secured("ROLE_verteiler")
    public String downloadApplicantDone(final KeycloakAuthenticationToken token, final Model model,
                                       @RequestParam("module") final String module,
                                       @RequestParam("applicants") final String applicant) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        }
        return webPdfService.getDownloadRedirectOfApplicantWithModule(module, applicant);
    }

    /**
     * Views main page
     *
     * @param token keycloak
     * @param model model
     * @return html
     */
    @Secured("ROLE_verteiler")
    @GetMapping("/uebersicht")
    public String distributerOverview(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            if (distributionService.isAllEmpty()) {
                model.addAttribute("message", "Es wurde noch keine Zuteilung vorgenommen!");
                model.addAttribute("type", "danger");
            }
            model.addAttribute("modules", moduleService.getModuleNames());
            model.addAttribute("modulesStudent", moduleService.getModuleNames());
        }
        return "pdf/pdfhandlingdistribution";
    }

    /**
     * Choose applicant after chosen module and downlaod pdf
     * @param token token
     * @param model model
     * @param module module
     * @return applicant download pdf
     */
    @Secured("ROLE_verteiler")
    @PostMapping("/downloadBewerberZugeteilt")
    public String downloadApplicantDistributed(final KeycloakAuthenticationToken token, final Model model,
                                    @RequestParam("modulesStudent") final String module) {

        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("module", module);
        model.addAttribute("applicants",
                dbDistributionService.findByModule(moduleService.findModuleByName(module)).getEmployees());

        return "pdf/pdfhandlingapplicantAssigned";
    }

    /**
     * Redirect to download all
     *
     * @param token keycloak
     * @param model model
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
            webPdfService.sendEmail(token, attributes, eMail, downloadProgress);
        }
        return new RedirectView("uebersicht", true);
    }

    /**
     * Returns a FileStream of the requested PDF.
     *
     * @param module   module name of the application.
     * @param student  student uniserial.
     * @param token    Keycloak token.
     * @param model    Model.
     * @param response HttpResponse
     */
    @Secured("ROLE_verteiler")
    @RequestMapping(value = "pdfDownload", method = RequestMethod.GET)
    public void fileSystemResource(
            @RequestParam(value = "module") final String module, @RequestParam(value = "student") final String student,
            final KeycloakAuthenticationToken token, final Model model,
            final HttpServletResponse response) throws IOException, NoSuchElementException {

        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            webPdfService.generatePdfForDownload(module, student, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * Downloads all applications for a module
     *
     * @param module   module
     * @param token    token
     * @param model    model
     * @param response HttpResponse
     */
    @Secured("ROLE_verteiler")
    @RequestMapping(value = "/zipModuleDownloadAssigned", method = RequestMethod.POST)
    public void zipDownloadAssignedModule(
            @RequestParam(value = "modules") final String module,
            final KeycloakAuthenticationToken token, final Model model,
            final HttpServletResponse response) throws IOException, NoSuchElementException {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            webPdfService.generateZipForModuleAssigned(response, module, downloadProgress);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }



    /**
     * Downloads all distributed as zip
     *
     * @param token    token
     * @param model    model
     * @param response HttpResponse
     */
    @Secured("ROLE_verteiler")
    @RequestMapping(value = "zipAllDistributedDownload", method = RequestMethod.GET)
    public void zipAllDistributedSystemResource(
            final KeycloakAuthenticationToken token, final Model model,
            final HttpServletResponse response) throws IOException, NoSuchElementException {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            webPdfService.generateZipForAllAssigned(response, downloadProgress);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * REST Response to downloadstatus of user download.
     *
     * @param token keycloaktoken.
     * @return Progress.
     */
    @ResponseBody
    @Secured("ROLE_verteiler")
    @GetMapping("/progress")
    public DownloadProgress getProgress(final KeycloakAuthenticationToken token) {
        if (token != null) {
            return downloadProgress;
        }
        return new DownloadProgress();
    }
}
