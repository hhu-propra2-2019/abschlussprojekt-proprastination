
package mops.controllers;

import mops.model.Account;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Role;
import mops.services.ApplicantService;
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
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionScope
@RequestMapping("/bewerbung2/bewerber")
public class ApplicationController {

    @Autowired
    private ApplicantService applicantServiceservice;

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
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("countries", CSVService.getCountries());
            model.addAttribute("courses", CSVService.getCourses());
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
     * Post Mapping after Pers Data (saves the applicant and provides input for module)
     * @param token keycloaktoken
     * @param model model to use
     * @param street street + number
     * @param place place
     * @param plz zipcode
     * @param birthplace birthplace
     * @param nationality nationality
     * @param birthday birthday
     * @param gender gender (weiblich or männlich)
     * @param course course the student is currently enrolled in
     * @param status employment status
     * @param graduation highest certificate reached yet
     * @param diverse commentary from applicant
     * @param modules module the applicant wants to apply for
     * @return module.html
     */
    @PostMapping("/modul")
    public String postModule(final KeycloakAuthenticationToken token, final Model model,
                             @RequestParam("street") final String street,
                             @RequestParam("place") final String place,
                             @RequestParam("plz") final String plz,
                             @RequestParam("placeofbirth") final String birthplace,
                             @RequestParam("nationality") final String nationality,
                             @RequestParam("birthday") final String birthday,
                             @RequestParam("gender") final String gender,
                             @RequestParam("courses") final String course,
                             @RequestParam("status") final String status,
                             @RequestParam("graduation") final String graduation,
                             @RequestParam("diverse") final String diverse,
                             @RequestParam("modules") final String modules) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("module", modules);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", CSVService.getModules());
            ApplicantService applicantService = new ApplicantService();
            Address address = Address.builder()
                    .street(street)
                    .city(place)
                    .zipcode(Integer.parseInt(plz))
                    .build();
            List<Application> applications = new ArrayList<>();
            Applicant applicant = applicantService.createApplicant(token.getName(), birthplace,
                    address, birthday, nationality, course, null, null, applications);
            model.addAttribute("applicant", applicant);
        }
        return "applicant/applicationModule";
    }

    /**
     * website for more modules, saves the former module and provides input for the next one
     * @param token keycloaktoken
     * @param model model
     * @param modules the module the applicant wants to apply for now
     * @param module the module the applicant applied for
     * @param workload hours the applicant may apply for
     * @param grade the grade the applicant had in the module
     * @param semester the semester the applicant completed the module
     * @param lecturer the lecturer the applicant wrote his exam with
     * @param tasks the role he wants to take
     * @param priority his priority
     * @param applicant probably not neccessary?
     * @return the same applicationModule.html
     */
    @PostMapping("/weiteresModul")
    public String weiteresModul(final KeycloakAuthenticationToken token,
                                 final Model model,
                                 @RequestParam("modules") final String modules,
                                 @RequestParam("module") final String module,
                                 @RequestParam("workload") final String workload,
                                 @RequestParam("grade") final String grade,
                                 @RequestParam("semesters") final String semester,
                                 @RequestParam("lecturer") final String lecturer,
                                 @RequestParam("tasks") final String tasks,
                                 @RequestParam("priority") final String priority,
                                 @RequestParam("applicant") final String applicant) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            model.addAttribute("module", modules);
            model.addAttribute("semesters", CSVService.getSemester());
            model.addAttribute("modules", CSVService.getModules());
            model.addAttribute("applicant", applicant);
            ApplicantService applicantService = new ApplicantService();
            Application application = applicantService.createApplication(token.getName(),
                    module,
                    lecturer,
                    semester,
                    null,
               //     Integer.parseInt(priority),
                    Integer.parseInt(workload),
                    Double.parseDouble(grade),
                    Role.KORREKTOR);
            System.out.println(applicant);
        }
        return "applicationModule";
    }

    /**
     * Overview, will be used to save the last module and shows the data the applicant filled in
     * @param token keycloaktone
     * @param model model
     * @param applicant applicant (load from database?)
     * @param module the module the applicant applied last for
     * @param workload look above
     * @param grade "
     * @param semester "
     * @param lecturer "
     * @param tasks "
     * @param priority "
     * @return overview.html
     */
    @PostMapping("/uebersicht")
    public String postOverview(final KeycloakAuthenticationToken token,
                               final Model model,
                               @RequestParam("applicant") final String applicant,
                               @RequestParam("module") final String module,
                               @RequestParam("workload") final String workload,
                               @RequestParam("grade") final String grade,
                               @RequestParam("semesters") final String semester,
                               @RequestParam("lecturer") final String lecturer,
                               @RequestParam("tasks") final String tasks,
                               @RequestParam("priority") final String priority
                               ) {
        if (token != null) {
            System.out.println(applicant);
            model.addAttribute("account", createAccountFromPrincipal(token));
            ApplicantService applicantService = new ApplicantService();
            Application application = applicantService.createApplication(token.getName(),
                    module,
                    lecturer,
                    semester,
                    "",
          //          Integer.parseInt(priority),
                    Integer.parseInt(workload),
                    Double.parseDouble(grade),
                    Role.KORREKTOR);
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
