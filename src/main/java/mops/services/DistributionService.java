package mops.services;

import mops.model.classes.Distribution;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributionService {

    private final DistributionRepository distributionRepository;
    private final ModuleService moduleService;
    private final ApplicantService applicantService;

    /**
     * Injects Services and repositories
     * @param distributionRepository the injected repository
     * @param moduleService the services that manages modules
     * @param applicantService the services that manages applicants
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DistributionService(final DistributionRepository distributionRepository,
                               final ModuleService moduleService,
                               final ApplicantService applicantService) {
        this.distributionRepository = distributionRepository;
        this.moduleService = moduleService;
        this.applicantService = applicantService;
        assign();
    }

    /**
     * Dummy functions that assignes applicants to Distributions
     */
    public void assign() {
        distributionRepository.save(Distribution.builder()
                .employees(applicantService.findAll())
                .module("unassigned")
                .build());
    }

    /**
     * Finds the Distribution of a model
     *
     * @param module the model
     * @return List of Distributions
     */
    public Distribution findByModule(final String module) {
        return distributionRepository.findByModule(module);
    }

    /**
     * Finds all Distributions
     *
     * @return List of Distributions
     */
    public List<Distribution> findAll() {
        return distributionRepository.findAll();
    }

    /**
     * Finds all Distributions that are unassigned
     *
     * @return List of Distributions
     */
    public Distribution findAllUnassigned() {
        return distributionRepository.findByModule("unassigned");
    }


}
