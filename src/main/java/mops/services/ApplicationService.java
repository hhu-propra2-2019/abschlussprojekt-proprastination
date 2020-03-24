package mops.services;

import mops.model.classes.Application;
import mops.model.classes.Application.ApplicationBuilder;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebApplication;
import mops.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicantService applicantService;
    private final ApplicationRepository applicationRepository;
    private final ModuleService moduleService;

    /**
     * Lets Spring inject the Repository and Service
     * @param applicantService the applicant service
     * @param applicationRepository the application repository
     * @param moduleService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ApplicationService(final ApplicantService applicantService,
                              final ApplicationRepository applicationRepository,
                              final ModuleService moduleService) {
        this.applicantService = applicantService;
        this.applicationRepository = applicationRepository;
        this.moduleService = moduleService;
    }

    /**
     * builds Application from webApplication
     * @param webApplication Informations
     * @return fully buildApplication
     */
    public Application buildApplication(final WebApplication webApplication) {
        System.out.println(webApplication.getModule());
        return Application.builder()
                //Module wird irgendwie nicht eingelesen? Mach ich später >_>
                .module(moduleService.findModuleByName(webApplication.getModule()))
                .minHours(webApplication.getMinHours())//HTML anpassen
                .maxHours(webApplication.getMaxHours())//HTML anpassen
                .priority(webApplication.getPriority())
                .grade(webApplication.getGrade())
                .lecturer(webApplication.getLecturer())
                .semester(webApplication.getSemester())
                .role(webApplication.getRole())
                .comment(webApplication.getComment())
                .build();
    }

    /**
     * Modifies application to the changes in webApplication.
     *
     * @param webApplication data to change.
     * @param application    Merging data into application
     * @return new application with changed data.
     */
    public Application changeApplication(final WebApplication webApplication, final Application application) {
        ApplicationBuilder applicationBuilder = application.toBuilder();
        return applicationBuilder
                .maxHours(webApplication.getMinHours())
                .minHours(webApplication.getMaxHours())
                .semester(webApplication.getSemester())
                .comment(webApplication.getComment())
                .grade(webApplication.getGrade())
                .lecturer(webApplication.getLecturer())
                .role(webApplication.getRole())
           //     .module(Module.builder().name(webApplication.getModule()).build())
                .priority(webApplication.getPriority())
                .build();
    }

    /**
     * @param application application
     */
    public void save(final Application application) {
        applicationRepository.save(application);
    }

    /**
     * Finds all Applications by Module id
     * @param id the module id
     * @return the list of applications
     */
    public List<Application> findAllByModuleId(final long id) {
        return applicationRepository.findAllByModule(moduleService.findById(id));
    }

    /**
     * Finds applications by ID
     * @param id the id
     * @return the application
     */
    public Application findById(final long id) {
        return applicationRepository.findById(id);
    }
}
