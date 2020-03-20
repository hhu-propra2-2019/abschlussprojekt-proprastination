package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DistributionService {

    private final DistributionRepository distributionRepository;
    private final ModuleService moduleService;
    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private final EvaluationService evaluationService;

    /**
     * Injects Services and repositories
     * @param distributionRepository the injected repository
     * @param moduleService the services that manages modules
     * @param applicantService the services that manages applicants
     * @param applicationService the services that manages applications
     * @param evaluationService the services that manages evaluations
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DistributionService(final DistributionRepository distributionRepository,
                               final ModuleService moduleService,
                               final ApplicantService applicantService,
                               final ApplicationService applicationService,
                               final EvaluationService evaluationService) {
        this.distributionRepository = distributionRepository;
        this.moduleService = moduleService;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.evaluationService = evaluationService;
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
     * automatically distributes applicants for one module
     * @param module
     * @return List of assigned applicants
     */
    private List<Applicant> distribute(final Module module) {
        List<Applicant> result = new LinkedList<>();
        List<Application> applications = applicationService.findApplicationsByModuleAndHours(module.getName(), 7);
        Map<Application, Integer> sortedApplications = new LinkedHashMap<>();
        int count = 0;

        for (Application application : applications) {
            int sum = application.getPriority() + evaluationService.findByApplication(application).getPriority();
            sortedApplications.put(application, sum);
        }
        sortedApplications.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedApplications.put(x.getKey(), x.getValue()));
        for (Application application : applications) {
            if (count >= module.getMax7()) {
                break;
            }
            result.add(application.getApplicant());
            count++;
        }

        applications = applicationService.findApplicationsByModuleAndHours(module.getName(), 9);
        sortedApplications = new LinkedHashMap<>();
        count = 0;

        for (Application application : applications) {
            int sum = application.getPriority() + evaluationService.findByApplication(application).getPriority();
            sortedApplications.put(application, sum);
        }
        sortedApplications.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedApplications.put(x.getKey(), x.getValue()));
        for (Application application : applications) {
            if (count >= module.getMax9()) {
                break;
            }
            result.add(application.getApplicant());
            count++;
        }

        applications = applicationService.findApplicationsByModuleAndHours(module.getName(), 17);
        sortedApplications = new LinkedHashMap<>();
        count = 0;

        for (Application application : applications) {
            int sum = application.getPriority() + evaluationService.findByApplication(application).getPriority();
            sortedApplications.put(application, sum);
        }
        sortedApplications.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedApplications.put(x.getKey(), x.getValue()));
        for (Application application : applications) {
            if (count >= module.getMax17()) {
                break;
            }
            result.add(application.getApplicant());
            count++;
        }

        return result;
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
