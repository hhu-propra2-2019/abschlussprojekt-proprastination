package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.CSVService;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.AccountGenerator;
import mops.services.webServices.StudentService;
import mops.services.webServices.WebApplicationService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/bewerber")
public class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private ApplicantService applicantService;
    private ModuleService moduleService;
    private ApplicationService applicationService;
    private StudentService studentService;
    private WebApplicationService webApplicationService;

    /**
     * Inits the service.
     *  @param applicantService   appservice
     * @param moduleService      moduleservice
     * @param applicationService appl. service
     * @param studentService     studentservice
     * @param webApplicationService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ApplicationController(final ApplicantService applicantService, final ModuleService moduleService,
                                 final ApplicationService applicationService, final StudentService studentService,
                                 final WebApplicationService webApplicationService) {
        this.applicantService = applicantService;
        this.moduleService = moduleService;
        this.applicationService = applicationService;
        this.studentService = studentService;
        this.webApplicationService = webApplicationService;
    }

    /**
     * GetMapping for the main board
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/")
    @Secured("ROLE_studentin")
    public String main(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = AccountGenerator.createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            model.addAttribute("applicant", applicantService.findByUniserial(account.getName()));
        }
        return "applicant/applicantMain";
    }

    /**
     * The GetMapping for a new application
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/neueBewerbung")
    @Secured("ROLE_studentin")
    public String newAppl(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            webApplicationService.creatNewApplicantIfNoneWasFound(
                    AccountGenerator.createAccountFromPrincipal(token), model);
        }
        return "applicant/applicationPersonal";
    }

    /**
     * The GetMapping for the open applicationsOverview
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/offeneBewerbungen")
    @Secured("ROLE_studentin")
    public String openAppl(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        }
        return "applicant/openAppl";
    }


    /**
     * The GetMapping for the personal data page
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/profil")
    @Secured("ROLE_studentin")
    public String personal(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        }
        return "applicant/personal";
    }

    /**
     * saves Applicant into database and waits for moduleinformation
     * @param token Keycloaktoken
     * @param webApplicant webApplicant and its data
     * @param applicantBindingResult the result of validating webApplicant
     * @param webAddress webAddress and its data
     * @param addressBindingResult the result of validating webAddress
     * @param webCertificate webCertificate and its data
     * @param certificateBindingResult the result of validating webCertificate
     * @param model Model
     * @param modules the module the Applicant wants to apply for
     * @return applicationModule.html
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    @PostMapping("/modul")
    @Secured("ROLE_studentin")
    public String modul(final KeycloakAuthenticationToken token, @Valid final WebApplicant webApplicant,
                        final BindingResult applicantBindingResult, @Valid final WebAddress webAddress,
                        final BindingResult addressBindingResult, final Model model, final String modules,
                        @Valid final WebCertificate webCertificate, final BindingResult certificateBindingResult) {

        webApplicationService.printBindingResultErrorsToLog(applicantBindingResult);
        webApplicationService.printBindingResultErrorsToLog(addressBindingResult);
        webApplicationService.printBindingResultErrorsToLog(certificateBindingResult);
        if (token != null) {
            webApplicationService.removeCurrentModuleFromListOfAvailebleModuleToApplyTo(
                    token, webApplicant, webAddress, model, modules, webCertificate);
        }
        if (applicantBindingResult.hasErrors() || addressBindingResult.hasErrors()
                || certificateBindingResult.hasErrors()) {
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
        }
        return "applicant/applicationModule";
    }



    /**
     * saves the current module application + calls for information for another module
     * @param token security token
     * @param webApplication the Application with its information
     * @param bindingResult the result of validating webApplication
     * @param model model
     * @param module the module the applicant wants to apply for next
     * @return html for another Modul
     */
    @PostMapping("weiteresModul")
    @Secured("ROLE_studentin")
    public String anotherModule(final KeycloakAuthenticationToken token,
                                @Valid final WebApplication webApplication, final BindingResult bindingResult,
                                final Model model,
                                @RequestParam("modules") final String module) {
        webApplicationService.returnErrorifMaxSmallerThenMin(webApplication, bindingResult, "WebApplication");
        if (bindingResult.hasErrors()) {
            webApplicationService.printErrorsIfPresent(bindingResult);

            Module modul = moduleService.findModuleByName(webApplication.getModule());
            Applicant applicant = applicantService.findByUniserial(token.getName());
            List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
            availableMods.remove(modul);

            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            model.addAttribute("newModule", modul);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", availableMods);
            model.addAttribute("webApplication", webApplication);
            return "applicant/applicationModule";
        }

        Module modul = moduleService.findModuleByName(module);
        Applicant applicant = applicantService.findByUniserial(token.getName());
        List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
        availableMods.remove(modul);

        Application application = studentService.buildApplication(webApplication);
        applicant = applicant.toBuilder().application(application).build();
        applicantService.saveApplicant(applicant);


        model.addAttribute("semesters", CSVService.getSemester());
        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("newModule", modul);
        model.addAttribute("modules", availableMods);
        model.addAttribute("webApplication", WebApplication.builder().module(module).build());
        return "applicant/applicationModule";
    }


    /**
     * Postmapping for editing personal data.
     * @param token keycloak
     * @param webApplicant applicant data
     * @param applicantBindingResult the result of validating webApplicant
     * @param webAddress address data
     * @param addressBindingResult the result of validating webAddress
     * @param webCertificate certificate data
     * @param certificateBindingResult the result of validating webCertificate
     * @param model model
     * @return webpage
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    @PostMapping("/uebersichtBearbeitet")
    @Secured("ROLE_studentin")
    public String saveOverview(final KeycloakAuthenticationToken token,
                               @Valid final WebApplicant webApplicant, final BindingResult applicantBindingResult,
                               @Valid final WebAddress webAddress, final BindingResult addressBindingResult,
                               @Valid final WebCertificate webCertificate, final BindingResult certificateBindingResult,
                               final Model model) {


        webApplicationService.printBindingResultErrorsToLog(applicantBindingResult);
        webApplicationService.printBindingResultErrorsToLog(addressBindingResult);
        webApplicationService.printBindingResultErrorsToLog(certificateBindingResult);

        if (applicantBindingResult.hasErrors() || addressBindingResult.hasErrors()
                || certificateBindingResult.hasErrors()) {
            if (token != null) {
                model.addAttribute("countries", CSVService.getCountries());
                model.addAttribute("courses", CSVService.getCourses());
                model.addAttribute("webApplicant", webApplicant);
                model.addAttribute("webAddress", webAddress);
                model.addAttribute("webCertificate", webCertificate);
            }
            return "applicant/applicationEditPersonal";
        }
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            studentService.savePersonalData(token, webApplicant, webAddress, webCertificate);
        }
        return "redirect:bewerbungsUebersicht";
    }


    /**
     * The GetMapping for the overview
     *
     * @param token      The KeycloakAuthentication
     * @param model      The Website model
     * @param applicant1 new Applicant Data
     * @return The HTML file rendered as a String
     */
    @PostMapping("/uebersichtDashboard")
    @Secured("ROLE_studentin")
    public String saveOverview(final KeycloakAuthenticationToken token, final Model model,
                               @ModelAttribute("applicant1") final Applicant applicant1) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            model.addAttribute("applicant", applicantService
                    .findByUniserial(token.getName()));
            studentService.updateApplicantWithoutChangingApplications(applicant1);
        }
        return "applicant/applicationOverview";
    }


    /**
     * Bewerbungsübersicht
     *
     * @param token keycloak.
     * @param model model.
     * @param error errormessage.
     * @return overview page.
     */
    @GetMapping("bewerbungsUebersicht")
    @Secured("ROLE_studentin")
    public String dashboardOverview(final KeycloakAuthenticationToken token, final Model model,
                                    @ModelAttribute("errormessage") final String error) {
        if (token != null) {
            Account account = AccountGenerator.createAccountFromPrincipal(token);
            Applicant applicant = applicantService.findByUniserial(account.getName());
            if (applicant == null) {
                return "redirect:";
            }
            model.addAttribute("errormessage", error);
            model.addAttribute("account", account);
            model.addAttribute("email", account.getEmail());
            model.addAttribute("applicant", applicant);
            model.addAttribute("modules",
                    studentService.getAllNotfilledModules(applicant, moduleService.getModules()));
        }
        return "applicant/applicationOverview";
    }

    /**
     * overview after Application is finished (also saves the last webApplication)
     * @param token the keycloak token
     * @param model the model
     * @param webApplication the last webApplication and its information
     * @param bindingResult the result of validating webApplication
     * @return the overviewhtml
     */
    @PostMapping("/uebersicht")
    @Secured("ROLE_studentin")
    public String overview(final KeycloakAuthenticationToken token, final Model model,
                           @Valid final WebApplication webApplication,
                           final BindingResult bindingResult) {
        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));


            webApplicationService.returnErrorifMaxSmallerThenMin(webApplication, bindingResult, "webApplication");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(err -> {
                    LOGGER.info("ERROR {}", err.getDefaultMessage());
                });
                Module modul = moduleService.findModuleByName(webApplication.getModule());
                Applicant applicant = applicantService.findByUniserial(token.getName());
                List<Module> availableMods = studentService.getAllNotfilledModules(applicant,
                        moduleService.getModules());
                availableMods.remove(modul);
                model.addAttribute("newModule", modul);
                model.addAttribute("semesters", CSVService.getSemester());
                model.addAttribute("modules", availableMods);
                model.addAttribute("webApplication", webApplication);
                return "applicant/applicationModule";
            }

            Applicant applicant = applicantService.findByUniserial(token.getName());
            Application application = studentService.buildApplication(webApplication);
            applicant = applicant.toBuilder().application(application).build();
            applicantService.saveApplicant(applicant);
            List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
            model.addAttribute("applicant", applicant);
            model.addAttribute("modules", availableMods);
        }
        return "applicant/applicationOverview";
    }

    /**
     *
     * @param token
     * @param model
     * @param modules
     * @return html
     */
    @PostMapping("/moduleNachUebersicht")
    @Secured("ROLE_studentin")
    public String postModuleAfterOverview(final KeycloakAuthenticationToken token, final Model model,
                                          @RequestParam("modules") final String modules) {
        Module module = moduleService.findModuleByName(modules);
        Applicant applicant = applicantService.findByUniserial(token.getName());
        List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
        availableMods.remove(module);
        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("newModule", module);
        model.addAttribute("semesters", CSVService.getSemester());
        model.addAttribute("modules", availableMods);
        model.addAttribute("webApplication", WebApplication.builder().module(modules).build());
        return "applicant/applicationModule";
    }

    /**
     * The GetMapping for the edit form fot personal data
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */
    @GetMapping("/bearbeitePersoenlicheDaten")
    @Secured("ROLE_studentin")
    public String editPersonalData(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            webApplicationService.creatNewApplicantIfNoneWasFound(
                    AccountGenerator.createAccountFromPrincipal(token), model);
        }
        return "applicant/applicationEditPersonal";
    }

    /**
     * @param id         id of module.
     * @param token      keycloaktoken
     * @param request    http request.
     * @param attributes redirect attributes
     * @return RedirectView.
     */
    @GetMapping("/bearbeiteModulDaten")
    @Secured("ROLE_studentin")
    public RedirectView validateEdit(@RequestParam("module") final long id, final KeycloakAuthenticationToken token,
                                     final HttpServletRequest request, final RedirectAttributes attributes) {
        if (token != null) {
            Account account = AccountGenerator.createAccountFromPrincipal(token);
            attributes.addFlashAttribute("account", account);
            Applicant applicant = applicantService.findByUniserial(account.getName());
            Application application = applicant.getApplicationById(id);

            if (application == null) {
                attributes.addFlashAttribute("errormessage", "Diese Bewerbung gehört dir nicht!");
                return new RedirectView("bewerbungsUebersicht", true);
            }
            if (application.getModule().getDeadline().isBefore(Instant.now())) {
                attributes.addFlashAttribute("errormessage", "Der Bewerbungszeitraum ist abgelaufen");
                return new RedirectView("bewerbungsUebersicht", true);
            }
            attributes.addFlashAttribute("id", Long.toString(id));
            attributes.addFlashAttribute("hello", "hello");
            return new RedirectView("bearbeiteModul");
        }
        return new RedirectView("bewerbungsUebersicht", true);
    }

    /**
     * Edit Module data
     *
     * @param id    application id
     * @param token keycloak
     * @param model model
     * @return Edit Module Page
     */
    @GetMapping("/bearbeiteModul")
    @Secured("ROLE_studentin")
    public String editModuleData(@ModelAttribute("id") final long id,
                                 final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Application application = applicationService.findById(id);
            Account account = AccountGenerator.createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("app", application);
        }
        return "applicant/applicationEditModule";
    }

    /**
     * Edits the given Application.
     *
     * @param webApplication Changes Data in WebApplication format.
     * @param bindingResult  The result of validating webApplication.
     * @param token          Keycloak.
     * @param model          Model.
     * @return mainpage.
     */
    @PostMapping(value = "/bearbeiteModulDaten")
    @Secured("ROLE_studentin")
    public String postEditModuledata(@Valid final WebApplication webApplication, final BindingResult bindingResult,
                                     final KeycloakAuthenticationToken token,
                                     final Model model) {

        webApplicationService.returnErrorifMaxSmallerThenMin(webApplication, bindingResult, "webApplication");

        webApplicationService.printBindingResultErrorsToLog(bindingResult);

        if (token != null) {
            model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
            Application application = applicationService.findById(webApplication.getId());
            Application newApplication = studentService.changeApplication(webApplication, application);
            applicationService.save(newApplication);
        }

        if (bindingResult.hasErrors()) {
            Application application = applicationService.findById(webApplication.getId());
            model.addAttribute("app", application);
            model.addAttribute("semesters", CSVService.getSemester());
            return "applicant/applicationEditModule";
        }
        return "redirect:bewerbungsUebersicht";
    }

    /**
     * Deletes Module from applicant.
     *
     * @param module module id.
     * @param token  Keycloak.
     * @param model  Model.
     * @return Mainpage.
     */
    @GetMapping("/loescheModul")
    @Secured("ROLE_studentin")
    public String delete(@RequestParam("module") final long module,
                         final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = AccountGenerator.createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            Applicant applicant = applicantService.findByUniserial(account.getName());
            Application application = applicant.getApplicationById(module);
            if (application == null) {
                return "redirect:bewerbungsUebersicht";
            }
            applicantService.deleteApplication(application, applicant);

        }
        return "redirect:bewerbungsUebersicht";
    }
}
