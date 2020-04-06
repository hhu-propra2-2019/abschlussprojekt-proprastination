package mops.services.webServices;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.dbServices.ModuleService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebModuleService {

    private final ModuleService moduleService;

    /**
     * Injects The Service
     *
     * @param moduleService the injected service
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebModuleService(final ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Finds all Modules
     *
     * @return List of all Modules
     */
    public List<WebModule> getModules() {
        List<Module> list = moduleService.getModules();
        List<WebModule> webList = new ArrayList<>();
        for (Module m: list) {
            webList.add(moduleService.toWebModule(m));
        }
        return webList;
    }

    /**
     * Saves a module
     *
     * @param webmodule module.
     */
    public void save(final WebModule webmodule) {
        concatenateDeadlinesWebModule(webmodule);
        moduleService.save(toModule(webmodule));
    }

    /** Sets the deadline (this should me reworked)
     *
     * @param webmodule current webmodule
     */
    private void concatenateDeadlinesWebModule(final WebModule webmodule) {
        if (webmodule.getOrgaDeadlineDate() == null || webmodule.getOrgaDeadlineTime() == null
        || webmodule.getApplicantDeadlineDate() == null || webmodule.getApplicantDeadlineTime() == null) {
            return;
        }
        webmodule.setOrgaDeadline(LocalDateTime.parse((String.format("%sT%s:00",
                webmodule.getOrgaDeadlineDate(), webmodule.getOrgaDeadlineTime()))));

        webmodule.setApplicantDeadline(LocalDateTime.parse((String.format("%sT%s:00",
                webmodule.getApplicantDeadlineDate(), webmodule.getApplicantDeadlineTime()))));
    }

    /**
     * saves an updated version of Module
     * @param webmodule edited module
     * @param oldName old name of module for finding id
     */
    public void update(final WebModule webmodule, final String oldName) {
        Module m = moduleService.findModuleByName(oldName);
        concatenateDeadlinesWebModule(webmodule);
        Module updated = toModule(webmodule);
        updated.setId(m.getId());
        moduleService.save(updated);
    }
    /**
     * Delete single module by name
     * @param name
     */
    public void deleteOne(final String name) {
        Module opt = moduleService.findModuleByName(name);
        moduleService.deleteById(opt.getId());
    }
    /**
     * deletes all modules
     */
    public void deleteAll() {
        moduleService.deleteAll();
    }

    /**
     * Return WebModule as module
     * @param webModule webmodule
     * @return Module generated Module
     */

    public Module toModule(final WebModule webModule) {
        return Module.builder()
                .name(webModule.getName())
                .shortName(webModule.getShortName())
                .profSerial(webModule.getProfSerial())
                .applicantDeadlineDate(webModule.getApplicantDeadlineDate())
                .applicantDeadlineTime(webModule.getApplicantDeadlineTime())
                .orgaDeadlineDate(webModule.getOrgaDeadlineDate())
                .orgaDeadlineTime(webModule.getOrgaDeadlineTime())
                .applicantDeadline(webModule.getApplicantDeadline())
                .orgaDeadline(webModule.getOrgaDeadline())
                .sevenHourLimit(webModule.getSevenHourLimit())
                .nineHourLimit(webModule.getNineHourLimit())
                .seventeenHourLimit(webModule.getSeventeenHourLimit())
                .build();
    }

    /**
     * Add an error to the binding result if applicant deadline is after orga deadline.
     * @param applicantDeadlineDate date of the deadline for applications as entered by the setup user (YYYY-MM-DD)
     * @param applicantDeadlineTime time of the deadline for applications as entered by the setup user (HH:MM)
     * @param orgaDeadlineDate date of the deadline for reviewing applications as entered by the setup user (YYYY-MM-DD)
     * @param orgaDeadlineTime time of the deadline for reviewing applications as entered by the setup user (HH:MM)
     * @param bindingResult the binding result to be returned
     * @param webModuleName name of the webModule to be added to the model
     */
    public void generateErrorIfApplicantDeadlineAfterOrgaDeadline(final String applicantDeadlineDate,
                                                                  final String applicantDeadlineTime,
                                                                  final String orgaDeadlineDate,
                                                                  final String orgaDeadlineTime,
                                                                  final BindingResult bindingResult,
                                                                  final String webModuleName) {
        LocalDateTime applicantDeadline;
        LocalDateTime orgaDeadline;

        try {
            applicantDeadline = LocalDateTime.parse(applicantDeadlineDate + "T" + applicantDeadlineTime);
        } catch (DateTimeParseException exception) {
            bindingResult.addError(new FieldError(webModuleName, "applicantDeadlineDate",
                    "Das Format der Bewerbungsfrist ist ungültig."));
            return;
        }

        try {
            orgaDeadline = LocalDateTime.parse(orgaDeadlineDate + "T" + orgaDeadlineTime);
        } catch (DateTimeParseException exception) {
            bindingResult.addError(new FieldError(webModuleName, "orgaDeadlineDate",
                    "Das Format der Bearbeitungsfrist ist ungültig."));
            return;
        }

        if (applicantDeadline.isAfter(orgaDeadline)) {
            bindingResult.addError(new FieldError(webModuleName, "applicantDeadlineDate",
                    "Die Bearbeitungsfrist darf nicht vor der Bewerbungsfrist sein."));
        }
    }
}
