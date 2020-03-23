package mops.services;

import mops.model.classes.Applicant;
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
                .minHours(webApplication.getFinalHours())//HTML anpassen
                .maxHours(webApplication.getFinalHours())//HTML anpassen
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
     * Finds application by uniserial and module
     * @param uniserial the applicant uniserial
     * @param module the module he applied in
     * @return the application
     */
    public Application findApplicatonByUniserialAndModule(final String uniserial, final String module) {
        return applicationRepository.findByApplicantAndModule(applicantService.findByUniserial(uniserial), module);
    }

    /**
     * Finds all application of an applicant
     *
     * @param applicant the applicant
     * @return his applications
     */
    public List<Application> findApplicationByApplicant(final Applicant applicant) {
        return applicationRepository.findAllByApplicant(applicant);
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
