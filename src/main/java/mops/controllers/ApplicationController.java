package mops.controllers;

import mops.model.Account;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.ApplicantService;
import mops.services.ApplicationService;
import mops.services.CSVService;
import mops.services.ModuleService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.HashSet;
import java.util.Set;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/bewerber")
public class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ApplicationService applicationService;

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
            WebApplicant webApplicant = WebApplicant.builder().build();
            WebAddress webAddress = WebAddress.builder().build();
            WebCertificate webCertificate = WebCertificate.builder().build();
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
            model.addAttribute("webApplicant", webApplicant);
            model.addAttribute("webAddress", webAddress);
            model.addAttribute("webCertificate", webCertificate);
            model.addAttribute("modules", CSVService.getModules());
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
                            @RequestParam("modules") final String modules) {

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
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("modul", modules);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", CSVService.getModules());
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
        }
        if (applicantBindingResult.hasErrors() || addressBindingResult.hasErrors()
                || certificateBindingResult.hasErrors()) {
            return "applicant/applicationPersonal";
        }
        Address address = applicantService.buildAddress(webAddress);
        Certificate certificate = applicantService.buildCertificate(webCertificate);
        Applicant applicant = applicantService.buildApplicant(token.getName(), webApplicant, address, certificate);
        applicantService.saveApplicant(applicant);
        model.addAttribute("webApplication", WebApplication.builder().build());
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
        Application application = applicationService.buildApplication(webApplication);
        Set<Application> applications = applicant.getApplications();
        applications.add(application);
        applicant.toBuilder().applications(applications);
        applicantService.saveApplicant(applicant);
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("modul", module);
        model.addAttribute("semesters", CSVService.getSemester());
        model.addAttribute("modules", CSVService.getModules());
        model.addAttribute("webApplication", webApplication);
        return "applicant/applicationModule";
    }

    /**
     * @param token
     * @param model
     * @param street
     * @param city
     * @param plz
     * @param birthplace
     * @param nationality
     * @param birthday
     * @param subject
     * @param status
     * @param graduation
     * @param graduationsubject
     * @param diverse
     * @return overview formular as String
     */

    @SuppressWarnings("checkstyle:ParameterNumber")
    @PostMapping("/uebersichtBearbeitet")
    public String saveOverview(final KeycloakAuthenticationToken token, final Model model,
                               @RequestParam("address1") final String street,
                               @RequestParam("address2") final String city,
                               @RequestParam("plz") final String plz,
                               @RequestParam("placeofbirth") final String birthplace,
                               @RequestParam("nationality") final String nationality,
                               @RequestParam("birthday") final String birthday,
                               @RequestParam("subject") final String subject,
                               @RequestParam("status") final String status,
                               @RequestParam("graduation") final String graduation,
                               @RequestParam("graduationsubject") final String graduationsubject,
                               @RequestParam("diverse") final String diverse) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            Address address = Address.builder().
                    street(street).
                    city(city).
                    zipcode(Integer.parseInt(plz)).
                    build();
            Certificate cert = Certificate.builder()
                    .name(graduation)
                    .course(graduationsubject)
                    .build();
            Set<Application> appls = new HashSet<>();
            Applicant applicant = Applicant.builder()
                    .birthplace(birthplace)
                    .address(address)
                    .birthday(birthday)
                    .nationality(nationality)
                    .course(subject)
                    .status("New")
                    .certs(cert)
                    .uniserial("has220")
                    .applications(appls)
                    .build();
            applicantService.updateApplicantWithoutChangingApplications(applicant);
            model.addAttribute("applicant", applicantService.findByUniserial("has220"));
        }
        return "applicant/applicationOverview";
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
            model.addAttribute("applicant", applicantService.findByUniserial("has220"));
            applicantService.updateApplicantWithoutChangingApplications(applicant1);
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
            Applicant applicant = applicantService.findByUniserial(account.getName());
            model.addAttribute("applicant", applicant);
            model.addAttribute("modules",
                    applicantService.getAllNotfilledModules(applicant, moduleService.getModules()));


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
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", CSVService.getModules());
            model.addAttribute("webApplication", webApplication);
            return "applicant/applicationModule";
        }

        Applicant applicant = applicantService.findByUniserial(token.getName());
        Application application = applicationService.buildApplication(webApplication);
        Set<Application> applications = applicant.getApplications();
        applications.add(application);
        applicant.toBuilder().applications(applications);
        applicantService.saveApplicant(applicant);
        model.addAttribute("applicant", applicant);
        return "applicant/applicationOverview";
    }
/*    /**
     * Overview, will be used to save the last module and shows the data the applicant filled in
     *
     * @param token     keycloaktone
     * @param model     model
     * @param applicant applicant (load from database?)
     * @param module    the module the applicant applied last for
     * @param workload  look above
     * @param grade     "
     * @param semester  "
     * @param lecturer  "
     *                  //  * @param tasks "
     *                  //  * @param priority "
     * @return overview.html
     */
 /*   @PostMapping("/uebersicht")
    @SuppressWarnings("checkstyle:ParameterNumber")
    public String postOverview(final KeycloakAuthenticationToken token,
                               final Model model,
                               @RequestParam("applicant") final Applicant applicant,
                               @RequestParam("module") final String module,
                               @RequestParam("workload") final String workload,
                               @RequestParam("grade") final String grade,
                               @RequestParam("semesters") final String semester,
                               @RequestParam("lecturer") final String lecturer
                               //                           @RequestParam("tasks") final String tasks,
                               //                           @RequestParam("priority") final String priority
    ) {
        if (token != null) {
            applicantService.saveApplicant(applicant);
            model.addAttribute("account", createAccountFromPrincipal(token));
            Application.builder()
                    .module(moduleService.findModuleByName(module))
                    .lecturer(lecturer)
                    .semester(semester)
                    .minHours(Integer.parseInt(workload))
                    .maxHours(Integer.parseInt(workload))
                    .grade(Double.parseDouble(grade))
                    .build();
        }
        return "applicant/applicationOverview";
    }*/


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
            model.addAttribute("account", account);
            model.addAttribute("applicant", applicantService.findByUniserial(account.getName()));
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
            Application newApplication = applicationService.changeApplication(webApplication, application);
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
