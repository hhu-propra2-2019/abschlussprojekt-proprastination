package mops.services.webServices;

import mops.controllers.ApplicationController;
import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebApplicationService {

    private final ModuleService moduleService;
    private final StudentService studentService;
    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    public WebApplicationService(ApplicantService applicantService, StudentService studentService,
                                 ModuleService moduleService, ApplicationService applicationService) {
        this.applicantService = applicantService;
        this.studentService = studentService;
        this.moduleService = moduleService;
        this.applicationService = applicationService;
    }

    public void printBindingResultErrorsToLog(BindingResult applicantBindingResult) {
        if (applicantBindingResult.hasErrors()) {
            applicantBindingResult.getAllErrors().forEach(err -> {
                LOGGER.info("ERROR {}", err.getDefaultMessage());
            });
        }
    }

    public void creatNewApplicantIfNoneWasFound(Account account, Model model) {

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

    public List<String> getApplicantUniserialsByModule(String module) {
        List<String> applicantUniserials = new ArrayList<>();
        for (Application application : applicationService.findApplicationsByModule(moduleService.findModuleByName(module))) {
            applicantUniserials.add(applicantService.findByApplications(application).getUniserial());
        }
        return applicantUniserials;
    }

    public void removeCurrentModuleFromListOfAvailebleModuleToApplyTo(KeycloakAuthenticationToken token, WebApplicant webApplicant,
                                                                      WebAddress webAddress, Model model, String modules, WebCertificate webCertificate) {
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

    public void printErrorsIfPresent(BindingResult bindingResult) {
        for (ObjectError err : bindingResult.getAllErrors()) {
            LOGGER.info("ERROR {}", err.getDefaultMessage());
        }
    }

    public void returnErrorifMaxSmallerThenMin(WebApplication webApplication, BindingResult bindingResult, String webApplication2) {
        if (webApplication.getMinHours() > webApplication.getMaxHours()) {
            bindingResult.addError(new FieldError(webApplication2, "maxHours",
                    "Maximale Stundenzahl darf nicht kleiner als minimale sein."));
        }
    }
}
