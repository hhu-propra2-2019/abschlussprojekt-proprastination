package mops.services;

import mops.model.classes.Application;
import mops.model.classes.Application.ApplicationBuilder;
import mops.model.classes.webclasses.WebApplication;
import mops.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

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
        return Application.builder()
                //Module wird irgendwie nicht eingelesen? Mach ich später >_>
                .module(webApplication.getModule())
                .minHours(webApplication.getFinalHours())//HTML anpassen
                .maxHours(webApplication.getFinalHours())//HTML anpassen
                .finalHours(webApplication.getFinalHours())
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
        return applicationBuilder.finalHours(webApplication.getFinalHours())
                .maxHours(webApplication.getFinalHours())
                .minHours(webApplication.getFinalHours())
                .semester(webApplication.getSemester())
                .comment(webApplication.getComment())
                .grade(webApplication.getGrade())
                .lecturer(webApplication.getLecturer())
                .role(webApplication.getRole())
                .module(webApplication.getModule())
                .priority(webApplication.getPriority())
                .build();
    }

    /**
     * as
     *
     * @param id as
     * @return Application
     */
    public Application findById(final long id) {
        return applicationRepository.findById(id).get();
    }

    /**
     * @param application application
     */
    public void save(final Application application) {
        applicationRepository.save(application);
    }
}
