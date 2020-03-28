package mops.services.webServices;

import mops.controllers.ApplicationController;
import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.CSVService;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.ModuleService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebApplicationService {

    private final ModuleService moduleService;
    private final StudentService studentService;
    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    /**
     * Constructor
     * @param applicantService
     * @param studentService
     * @param moduleService
     * @param applicationService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebApplicationService(final ApplicantService applicantService, final StudentService studentService,
                                 final ModuleService moduleService, final ApplicationService applicationService) {
        this.applicantService = applicantService;
        this.studentService = studentService;
        this.moduleService = moduleService;
        this.applicationService = applicationService;
    }

    /**
     * -
     * @param applicantBindingResult
     */
    public void printBindingResultErrorsToLog(final BindingResult applicantBindingResult) {
        if (applicantBindingResult.hasErrors()) {
            applicantBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }
    }

    /**
     * -
     * @param account
     * @param model
     */
    public void createNewApplicantIfNoneWasFound(final Account account, final Model model) {

        Applicant applicant = applicantService.findByUniserial(account.getName());

        WebApplicant webApplicant = (applicant == null)
                ? WebApplicant.builder().build() : studentService.getExistingApplicant(applicant);
        WebAddress webAddress = (applicant == null)
                ? WebAddress.builder().build() : studentService.getExistingAddress(applicant.getAddress());
        WebCertificate webCertificate = (applicant == null)
                ? WebCertificate.builder().build() : studentService.getExistingCertificate(applicant.getCerts());
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

    /**
     * -
     * @param module
     * @return -
     */
    public List<String> getApplicantUniserialsByModule(final String module) {
        return applicationService.
                findApplicationsByModule(moduleService
                        .findModuleByName(module))
                .stream()
                .map(application -> applicantService
                        .findByApplications(application)
                        .getUniserial())
                .collect(Collectors.toList());
    }

    /**
     * -
     * @param token
     * @param webApplicant
     * @param webAddress
     * @param model
     * @param modules
     * @param webCertificate
     */
    public void removeCurrentModuleFromListOfAvailebleModuleToApplyTo(final KeycloakAuthenticationToken token,
                                                                      final WebApplicant webApplicant,
                                                                      final WebAddress webAddress,
                                                                      final Model model, final String modules,
                                                                      final WebCertificate webCertificate) {
        Applicant applicant = studentService.savePersonalData(token, webApplicant, webAddress, webCertificate);
        Module module = moduleService.findModuleByName(modules);
        List<Module> availableMods = studentService.getAllNotfilledModules(applicant, moduleService.getModules());
        availableMods.remove(module);

        model.addAttribute("account", AccountGenerator.createAccountFromPrincipal(token));
        model.addAttribute("newModule", module);
        model.addAttribute("semesters", CSVService.getSemester());
        model.addAttribute("modules", availableMods);
        model.addAttribute("webApplication", WebApplication.builder().module(modules).build());
    }

    /**
     * -
     * @param bindingResult
     */
    public void printErrorsIfPresent(final BindingResult bindingResult) {
        for (ObjectError err : bindingResult.getAllErrors()) {
            LOGGER.info("ERROR {}", err.getDefaultMessage());
        }
    }

    /**
     * -
     * @param webApplication
     * @param bindingResult
     * @param webApplication2
     */
    public void returnErrorIfMaxSmallerThenMin(final WebApplication webApplication,
                                               final BindingResult bindingResult,
                                               final String webApplication2) {
        if (webApplication.getMinHours() > webApplication.getMaxHours()) {
            bindingResult.addError(new FieldError(webApplication2, "maxHours",
                    "Maximale Stundenzahl darf nicht kleiner als minimale sein."));
        }
    }
}
