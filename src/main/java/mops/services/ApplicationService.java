package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
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
     * Finds application by module and hours
     * @param module the module
     * @return the applications
     */
    public List<Application> findApplicationsByModule(final String module) {
        return applicationRepository.findByModule(module);
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
     * @param applicant the applicant
     * @return his applications
     */
    public List<Application> findApplicationByApplicant(final Applicant applicant) {
        return applicationRepository.findAllByApplicant(applicant);
    }
}
