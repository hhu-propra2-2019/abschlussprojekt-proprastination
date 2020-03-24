package mops.services;

import mops.model.classes.Application;
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
