package mops.services;

import mops.model.classes.Application;
import mops.model.classes.Module;
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

    /**
     * Finds application by module and hours
     * @param module the module
     * @return the applications
     */
    public List<Application> findApplicationsByModule(final Module module) {
        return applicationRepository.findByModule(module);
    }

    /**
     * saves/updates application
     * @param application new Application
     */
    public void save(final Application application) {
        applicationRepository.save(application);
    }

    /**
     * finds all applications
     * @return all applications as List
     */
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }
}
