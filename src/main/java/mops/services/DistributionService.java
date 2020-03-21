package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * distributes the Applicants
     */
    private void distribute2() {
        List<Module> modules = moduleService.getModules();
        for (Module module : modules) {
            List<Evaluation> evaluations = new LinkedList<>();
            List<Application> applications = applicationService.findApplicationsByModule(module.getName());
            for (Application application : applications) {
                Evaluation evaluation = evaluationService.findByApplication(application);
                evaluations.add(evaluation);
            }

            List<Evaluation>[] sortedByOrgaPrio = new LinkedList[4];

            for (int i = 0; i < 4; i++) {
                sortedByOrgaPrio[i] = new LinkedList<>();
            }

            //List<Evaluation> orgaPrio1 = new LinkedList<>();
            //List<Evaluation> orgaPrio2 = new LinkedList<>();
            //List<Evaluation> orgaPrio3 = new LinkedList<>();
            //List<Evaluation> orgaPrio4 = new LinkedList<>();

            for (Evaluation evaluation : evaluations) {
                sortedByOrgaPrio[evaluation.getPriority()].add(evaluation);
                /*
                if (evaluation.getPriority() == 1) {
                    orgaPrio1.add(evaluation);
                } else if (evaluation.getPriority() == 2) {
                    orgaPrio2.add(evaluation);
                } else if (evaluation.getPriority() == 3) {
                    orgaPrio3.add(evaluation);
                } else if (evaluation.getPriority() == 4) {
                    orgaPrio4.add(evaluation);
                } */
            }

            for (int i = 0; i < 4; i++) {
                sortedByOrgaPrio[i].sort(Comparator.comparing(a -> a.getApplication().getPriority()));
            }

            /*orgaPrio1.sort(Comparator.comparing(a -> a.getApplication().getPriority()));
            orgaPrio2.sort(Comparator.comparing(a -> a.getApplication().getPriority()));
            orgaPrio3.sort(Comparator.comparing(a -> a.getApplication().getPriority()));
            orgaPrio4.sort(Comparator.comparing(a -> a.getApplication().getPriority()));*/

            int count7 = 0;
            int count9 = 0;
            int count17 = 0;

            List<Applicant> distributedApplicants = new LinkedList<>();

            for (int i = 0; i < 4; i++) {
                if (count7 == module.getMax7() && count9 == module.getMax9() && count17 == module.getMax17()) {
                    break;
                }
                for (Evaluation ev : sortedByOrgaPrio[i]) {
                    if (count7 == module.getMax7() && count9 == module.getMax9() && count17 == module.getMax17()) {
                        break;
                    }
                    if (ev.getHours() == 7 && count7 < modules.getMax7()) {
                        distributedApplicants.add(ev.getApplication().getApplicant());
                        count7++;
                    }
                    if (ev.getHours() == 9 && count7 < modules.getMax9()) {
                        distributedApplicants.add(ev.getApplication().getApplicant());
                        count9++;
                    }
                    if (ev.getHours() == 17 && count7 < modules.getMax17()) {
                        distributedApplicants.add(ev.getApplication().getApplicant());
                        count17++;
                    }
                }
            }

            distributionRepository.save(Distribution.builder()
                    .employees(distributedApplicants)
                    .module(module.getName())
                    .build());
        }
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
