package mops.controllers;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.ApplicantService;
import mops.services.ApplicationService;
import mops.services.CSVService;
import mops.services.ModuleService;
import mops.services.StudentService;
import org.keycloak.KeycloakPrincipal;
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

import javax.validation.Valid;
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


    /**
     * Inits the service.
     *
     * @param applicantService   appservice
     * @param moduleService      moduleservice
     * @param applicationService appl. service
     * @param studentService     studentservice
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ApplicationController(final ApplicantService applicantService, final ModuleService moduleService,
                                 final ApplicationService applicationService, final StudentService studentService) {
        this.applicantService = applicantService;
        this.moduleService = moduleService;
        this.applicationService = applicationService;
        this.studentService = studentService;
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
            Account account = createAccountFromPrincipal(token);
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
            Account account = createAccountFromPrincipal(token);
            Applicant applicant = applicantService.findByUniserial(account.getName());

            WebApplicant webApplicant = (applicant == null)
                    ? WebApplicant.builder().build() : studentService.getExsistingApplicant(applicant);
            WebAddress webAddress = (applicant == null)
                    ? WebAddress.builder().build() : studentService.getExsistingAddress(applicant.getAddress());
            WebCertificate webCertificate = (applicant == null)
                    ? WebCertificate.builder().build() : studentService.getExsistingCertificate(applicant.getCerts());
            List<Module> modules = (applicant == null)
                    ? moduleService.getModules() : studentService
                    .getAllNotfilledModules(applicant, moduleService.getModules());

            model.addAttribute("account", account);
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
            model.addAttribute("webApplicant", webApplicant);
            model.addAttribute("webAddress", webAddress);
            model.addAttribute("webCertificate", webCertificate);
            model.addAttribute("modules", modules);
        }
        return "applicant/applicationPersonal";
    }

    /**
     * The GetMapping for the open applications
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */

    @GetMapping("/offeneBewerbungen")
    public String openAppl(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
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
    public String personal(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
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
    public String modul(final KeycloakAuthenticationToken token,
                            @Valid final WebApplicant webApplicant, final BindingResult applicantBindingResult,
                            @Valid final WebAddress webAddress, final BindingResult addressBindingResult,
                            final Model model,
                            @Valid final WebCertificate webCertificate, final BindingResult certificateBindingResult,
                            final String modules) {

        if (applicantBindingResult.hasErrors()) {
            applicantBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }

        if (addressBindingResult.hasErrors()) {
            addressBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }

        if (certificateBindingResult.hasErrors()) {
            certificateBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }

        if (token != null) {

            Applicant applicant = studentService.savePersonalData(token, webApplicant, webAddress, webCertificate);
            Module module = moduleService.findModuleByName(modules);
            List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
            availableMods.remove(module);

            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("newModule", module);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", availableMods);
            model.addAttribute("webApplication", WebApplication.builder().module(modules).build());
        }
        if (applicantBindingResult.hasErrors() || addressBindingResult.hasErrors()
                || certificateBindingResult.hasErrors()) {
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
            return "applicant/applicationPersonal";
        }
       return "applicant/applicationModule";
    }

    /**
     * saves the current module application + calls for information for another module
     * @param token security token
     * @param webApplication the Application with its information
     * @param model model
     * @param module the module the applicant wants to apply for next
     * @return html for another Modul
     */
    @PostMapping("weiteresModul")
    public String anotherModule(final KeycloakAuthenticationToken token,
                              final WebApplication webApplication, final Model model,
                              @RequestParam("modules") final String module) {
        Applicant applicant = applicantService.findByUniserial(token.getName());
        Application application = studentService.buildApplication(webApplication);
        applicant = applicant.toBuilder().application(application).build();
        applicantService.saveApplicant(applicant);

        Module modul = moduleService.findModuleByName(module);
        List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
        availableMods.remove(modul);

        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("newModule", modul);
        model.addAttribute("semesters", CSVService.getSemester());
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
    public String saveOverview(final KeycloakAuthenticationToken token,
                               @Valid final WebApplicant webApplicant, final BindingResult applicantBindingResult,
                               @Valid final WebAddress webAddress, final BindingResult addressBindingResult,
                               @Valid final WebCertificate webCertificate, final BindingResult certificateBindingResult,
                               final Model model) {


        if (applicantBindingResult.hasErrors()) {
            applicantBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }

        if (addressBindingResult.hasErrors()) {
            addressBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }

        if (certificateBindingResult.hasErrors()) {
            certificateBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }

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
            model.addAttribute("account", createAccountFromPrincipal(token));

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
    public String saveOverview(final KeycloakAuthenticationToken token, final Model model,
                               @ModelAttribute("applicant1") final Applicant applicant1) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("applicant", applicantService.findByUniserial("has220"));        //?!?! WHAT IS DIS
            studentService.updateApplicantWithoutChangingApplications(applicant1);
        }
        return "applicant/applicationOverview";
    }

    /**
     * getmapping for overview
     * @param token
     * @param model
     * @return overview html as string
     */
    @GetMapping("bewerbungsUebersicht")
    public String dashboardOverview(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            model.addAttribute("email", account.getEmail());
            Applicant applicant = applicantService.findByUniserial(account.getName());
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
    public String overview(final KeycloakAuthenticationToken token, final Model model,
                           @Valid final WebApplication webApplication,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
            Module module = moduleService.findModuleByName(webApplication.getModule());
            model.addAttribute("newModule", module);
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", CSVService.getModules());
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
    public String postModuleAfterOverview(final KeycloakAuthenticationToken token, final Model model,
                                          @RequestParam("modules") final String modules) {
        Module module = moduleService.findModuleByName(modules);
        Applicant applicant = applicantService.findByUniserial(token.getName());
        List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
        availableMods.remove(module);
        model.addAttribute("account", createAccountFromPrincipal(token));
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
    public String editPersonalData(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            Applicant applicant = applicantService.findByUniserial(account.getName());

            WebApplicant webApplicant = (applicant == null)
                    ? WebApplicant.builder().build() : studentService.getExsistingApplicant(applicant);
            WebAddress webAddress = (applicant == null)
                    ? WebAddress.builder().build() : studentService.getExsistingAddress(applicant.getAddress());
            WebCertificate webCertificate = (applicant == null)
                    ? WebCertificate.builder().build() : studentService.getExsistingCertificate(applicant.getCerts());
            List<Module> modules = (applicant == null)
                    ? moduleService.getModules() : studentService
                    .getAllNotfilledModules(applicant, moduleService.getModules());

            model.addAttribute("account", account);
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
            model.addAttribute("webApplicant", webApplicant);
            model.addAttribute("webAddress", webAddress);
            model.addAttribute("webCertificate", webCertificate);
            model.addAttribute("modules", modules);
        }
        return "applicant/applicationEditPersonal";
    }

    /**
     * The GetMapping for the edit form for modules
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @param id    module id.
     * @return The HTML file rendered as a String
     */

    @GetMapping("/bearbeiteModulDaten")
    public String editModuleData(@RequestParam("module") final long id,
                                 final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            Applicant applicant = applicantService.findByUniserial(account.getName());
            Application application = applicant.getApplicationById(id);
            if (application == null) {
                return "redirect:bewerbungsUebersicht";
            }
            model.addAttribute("app", application);
        }
        return "applicant/applicationEditModule";
    }

    /**
     * Edits the given Application.
     *
     * @param webApplication Changes Data in WebApplication format.
     * @param token          Keycloak.
     * @param model          Model.
     * @return mainpage.
     */
    @PostMapping(value = "/bearbeiteModulDaten")
    public String postEditModuledata(final WebApplication webApplication, final KeycloakAuthenticationToken token,
                                     final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            Application application = applicationService.findById(webApplication.getId());
            Application newApplication = studentService.changeApplication(webApplication, application);
            applicationService.save(newApplication);
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
    public String delete(@RequestParam("module") final long module,
                         final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
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
