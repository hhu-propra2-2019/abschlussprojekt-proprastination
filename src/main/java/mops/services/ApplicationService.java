package mops.services;

import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebApplication;
import mops.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
                              final ApplicationRepository applicationRepository, ModuleService moduleService) {
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
        Application application = Application.builder()
                //Module wird irgendwie nicht eingelesen? Mach ich später >_>
                .module(webApplication.getModule())
                .minHours(webApplication.getWorkload())//HTML anpassen
                .maxHours(webApplication.getWorkload())//HTML anpassen
                .priority(webApplication.getPriority())
                .grade(webApplication.getGrade())
                .lecturer(webApplication.getLecturer())
                .semester(webApplication.getSemester())
                .role(webApplication.getRole())
                .comment(webApplication.getComment())
                .build();
        return application;
    }

    public List<Application> findAllByModuleId(final long id) {
        return applicationRepository.findAllByModule("RDB");
    }

    public Application findById(final long id) {
        return applicationRepository.findById(id);
    }
}
