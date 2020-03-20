package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApplicationService(final ApplicantService applicantService,
                              final ApplicationRepository applicationRepository) {
        this.applicantService = applicantService;
        this.applicationRepository = applicationRepository;
    }

    public Application findApplicatinByUniserialAndModule(final String uniserial, final String module) {
        return applicationRepository.findByApplicantAndModule(applicantService.findByUniserial(uniserial), module);
    }

    public List<Application> findApplicationByApplicant(final Applicant applicant) {
        return applicationRepository.findAllByApplicant(applicant);
    }
}
