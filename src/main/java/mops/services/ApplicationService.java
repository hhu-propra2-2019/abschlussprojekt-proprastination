package mops.services;

import mops.model.classes.Application;
import mops.model.classes.webclasses.WebApplication;
import mops.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicantService applicantService;
    private final ApplicationRepository applicationRepository;

    /**
     * Lets Spring inject the Repository and Service
     * @param applicantService the applicant service
     * @param applicationRepository the application repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ApplicationService(final ApplicantService applicantService,
                              final ApplicationRepository applicationRepository) {
        this.applicantService = applicantService;
        this.applicationRepository = applicationRepository;
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

    /**
     * Finds application by module and hours
     * @param module the module
     * @return the applications
     */
    public List<Application> findApplicationsByModule(final String module) {
        return applicationRepository.findByModule(module);
    }

    public void save(final Application application) {
        applicationRepository.save(application);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }
}
