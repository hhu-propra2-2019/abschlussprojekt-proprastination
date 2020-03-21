package mops.controllers;

import mops.model.Account;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.services.ApplicantService;
import mops.services.ApplicationService;
import mops.services.CSVService;
import org.keycloak.KeycloakPrincipal;
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
import java.util.Set;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/bewerber")
public class ApplicationController {

    @Autowired
    private ApplicantService applicantService;
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
            model.addAttribute("account", createAccountFromPrincipal(token));
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
            WebApplicant webApplicant = WebApplicant.builder()
                    .build();
            WebAddress webAddress = WebAddress.builder().build();
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
            model.addAttribute("webApplicant", webApplicant);
            model.addAttribute("webAddress", webAddress);
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
     * @param webAddress webAddress and its data
     * @param model Model
     * @param modules the module the Applicant wants to apply for
     * @return applicationModule.html
     */
    @PostMapping("/modul")
    @Secured("ROLE_studentin")
    public String modul(final KeycloakAuthenticationToken token, final WebApplicant webApplicant,
                            final WebAddress webAddress, final Model model,
                            @RequestParam("modules") final String modules) {

        if (token != null) {
            Address address = applicantService.buildAddress(webAddress);
            Applicant applicant = applicantService.buildApplicant(token.getName(), webApplicant, address);
            applicantService.saveApplicant(applicant);
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("modul", modules);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", CSVService.getModules());
            model.addAttribute("webApplication", WebApplication.builder().build());
        }
        return "applicant/applicationModuleThymeleaf";
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
    public String weiteresModul(final KeycloakAuthenticationToken token,
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
        return "applicant/applicationModuleThymeleaf";
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
            model.addAttribute("applicant", applicantService.findByUniserial(account.getName()));

        }
        return "applicant/applicationOverview";
    }

    /**
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
    @PostMapping("/uebersicht")
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
                    .module(module)
                    .lecturer(lecturer)
                    .semester(semester)
                    .minHours(Integer.parseInt(workload))
                    .maxHours(Integer.parseInt(workload))
                    .grade(Double.parseDouble(grade))
                    .build();
        }
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
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("applicant", applicantService.findByUniserial("has220"));
        }
        return "applicant/applicationEditPersonal";
    }

    /**
     * The GetMapping for the edit form for modules
     *
     * @param token The KeycloakAuthentication
     * @param model The Website model
     * @return The HTML file rendered as a String
     */

    @GetMapping("/bearbeiteModulDaten")
    public String editModuleData(final KeycloakAuthenticationToken token, final Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "applicant/applicationEditPersonal";
    }
}
