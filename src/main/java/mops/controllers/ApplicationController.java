package mops.controllers;

import mops.model.Account;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
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
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/bewerber")
public class ApplicationController {

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private StudentService studentService;

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
     * @param webAddress webAddress and its data
     * @param webCertificate webCertificate and its data
     * @param model Model
     * @param modules the module the Applicant wants to apply for
     * @return applicationModule.html
     */
    @PostMapping("/modul")
    @Secured("ROLE_studentin")
    public String modul(final KeycloakAuthenticationToken token, final WebApplicant webApplicant,
                        final WebAddress webAddress, final WebCertificate webCertificate, final Model model,
                        final String modules) {

        if (token != null) {
            OidcKeycloakAccount account = token.getAccount();
            String givenName = account.getKeycloakSecurityContext().getIdToken().getGivenName();
            String familyName = account.getKeycloakSecurityContext().getIdToken().getFamilyName();

            Module module = moduleService.findModuleByName(modules);

            Address address = studentService.buildAddress(webAddress);
            Certificate certificate = studentService.buildCertificate(webCertificate);
            Applicant applicant = studentService.buildApplicant(token.getName(), webApplicant,
                    address, certificate, givenName, familyName);
            applicantService.saveApplicant(applicant);
            List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
            availableMods.remove(module);

            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("newModule", module);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", availableMods);
            model.addAttribute("webApplication", WebApplication.builder().module(modules).build());
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
                    zipcode(plz).
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
            studentService.updateApplicantWithoutChangingApplications(applicant);
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
     * @return the overviewhtml
     */
    @PostMapping("/uebersicht")
    public String overview(final KeycloakAuthenticationToken token, final Model model,
                           final WebApplication webApplication) {
        Applicant applicant = applicantService.findByUniserial(token.getName());
        Application application = studentService.buildApplication(webApplication);
        applicant = applicant.toBuilder().application(application).build();
        applicantService.saveApplicant(applicant);
        model.addAttribute("applicant", applicant);
        return "applicant/applicationOverview";
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
            Address address = applicant.getAddress();
            WebApplicant webApplicant = studentService.getExsistingApplicant(applicant);
            WebAddress webAddress =  studentService.getExsistingAddress(address);
            WebCertificate webCertificate = studentService.getExsistingCertificate(applicant.getCerts());
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
            model.addAttribute("webCertificate", webCertificate);
            model.addAttribute("account", account);
            model.addAttribute("webApplicant", webApplicant);
            model.addAttribute("webAddress", webAddress);
        }
        return "applicant/applicationEditPersonal";
    }

    /**
     * updates new personal data in database
     * @param token token
     * @param model model
     * @param webAddress address information
     * @param webApplicant applicant information
     * @param webCertificate certificate information
     * @return go back to overview
     */
    @PostMapping("/uebersichtnachbearbeitungPersDaten")
    public String postEditPersonalData(final KeycloakAuthenticationToken token, final Model model,
                                       final WebAddress webAddress, final WebApplicant webApplicant,
                                       final WebCertificate webCertificate) {
        if (token != null) {
            OidcKeycloakAccount account = token.getAccount();
            String givenName = account.getKeycloakSecurityContext().getIdToken().getGivenName();
            String familyName = account.getKeycloakSecurityContext().getIdToken().getFamilyName();
            Address address = studentService.buildAddress(webAddress);
            System.out.println(address);
            Certificate certificate = studentService.buildCertificate(webCertificate);
            Applicant applicant = studentService.buildApplicant(token.getName(), webApplicant, address, certificate,
                    givenName, familyName);
            System.out.println(applicant);
            model.addAttribute("applicant", applicant);
            applicantService.saveApplicant(applicant);
        }
        return "applicant/applicationOverview";
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
