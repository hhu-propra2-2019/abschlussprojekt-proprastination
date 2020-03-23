package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Applicant.ApplicantBuilder;
import mops.model.classes.Application;
import mops.model.classes.Application.ApplicationBuilder;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.model.classes.webclasses.WebDistributorApplication;
//import mops.model.classes.Module;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class DistributionService {

    private final DistributionRepository distributionRepository;
    private final ModuleService moduleService;
    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private final EvaluationService evaluationService;
    private final int numberOfPriorities = 4;
    private final int sevenHours = 7;
    private final int nineHours = 9;
    private final int seventeenHours = 17;

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
        distribute();
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
     * distributes the Applicants
     */
    private void distribute() {
        //List<Module> modules = moduleService.getModules();
                                                                                                                        List<String> modules = CSVService.getModules();
        List<Applicant> allApplicants = applicantService.findAll();
        for (String module : modules) {
            List<Evaluation> evaluations = new LinkedList<>();
            //List<Application> preApplications = applicationService.findApplicationsByModule(module);
                                                                                                                        List<Application> preApplications = new LinkedList<>();
                                                                                                                        for (Applicant applicant : allApplicants) {
                                                                                                                            preApplications.add(applicant.getApplications().iterator().next());
                                                                                                                        }
            List<Application> applications = new LinkedList<>();
            for (Application application : preApplications) {
                if (allApplicants.indexOf(applicantService.findByApplications(application)) != -1) {
                    applications.add(application);
                }
            }
            for (Application application : applications) {
                Evaluation evaluation = evaluationService.findByApplication(application);
                evaluations.add(evaluation);
            }

            List<Evaluation>[] sortedByOrgaPrio = new List[numberOfPriorities];

            for (int i = 0; i < numberOfPriorities; i++) {
                sortedByOrgaPrio[i] = new LinkedList<>();
            }

            for (Evaluation evaluation : evaluations) {
                sortedByOrgaPrio[evaluation.getPriority() - 1].add(evaluation);
            }

            for (int i = 0; i < numberOfPriorities; i++) {
                sortedByOrgaPrio[i].sort(Comparator.comparing(a -> a.getApplication().getPriority()));
            }

            int count7 = 0;
            int count9 = 0;
            int count17 = 0;

            Set<Applicant> distributedApplicants = new LinkedHashSet<>();

            for (int i = 0; i < numberOfPriorities; i++) {
                if (count7 == 4 && count9 == 5 && count17 == 6) {
                    break;
                }
                for (Evaluation evaluation : sortedByOrgaPrio[i]) {
                    if (count7 == 4 && count9 == 5 && count17 == 6) {
                        break;
                    }
                    Applicant applicant = applicantService.findByApplications(evaluation.getApplication());
                    if (evaluation.getHours() == sevenHours && count7 < 4) {
                        distributedApplicants.add(applicant);
                        allApplicants.remove(applicant);
                        changeFinalHours(evaluation);
                        count7++;
                    } else if (evaluation.getHours() == nineHours && count9 < 5) {
                        distributedApplicants.add(applicant);
                        allApplicants.remove(applicant);
                        changeFinalHours(evaluation);
                        count9++;
                    } else if (evaluation.getHours() == seventeenHours && count17 < 6) {
                        distributedApplicants.add(applicant);
                        allApplicants.remove(applicant);
                        changeFinalHours(evaluation);
                        count17++;
                    }
                }
            }
            distributionRepository.save(Distribution.builder()
                    .employees(distributedApplicants)
                    .module(module)
                    .build());
        }
        distributionRepository.save(Distribution.builder()
                .employees(allApplicants)
                .module("unassigned")
                .build());
    }

    /**
     * changes finalHours in application
     * @param evaluation
     */
    private void changeFinalHours(final Evaluation evaluation) {
        ApplicationBuilder applicationBuilder = evaluation.getApplication().toBuilder();
        Application application = applicationBuilder.finalHours(evaluation.getHours()).build();
        applicationService.save(application);
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

    /**
     * converts Distributions to Web Distributions
     * @return List of WebDistributions
     */

    public List<WebDistribution> convertDistributionsToWebDistributions() {
        List<WebDistribution> webDistributionList = new ArrayList<>();
        List<Distribution> distributionList = findAll();
        for (Distribution distribution : distributionList) {
            List<WebDistributorApplicant> webDistributorApplicantList =
                    convertApplicantToWebDistributorApplicant(distribution.getEmployees(), distribution.getModule());
            WebDistribution webDistribution = WebDistribution.builder()
                    .module(distribution.getModule())
                    .webDistributorApplicants(webDistributorApplicantList)
                    .build();
            webDistributionList.add(webDistribution);
        }
        return webDistributionList;
    }

    private List<WebDistributorApplicant> convertApplicantToWebDistributorApplicant(
            final List<Applicant> applicantList, final String module) {
        List<WebDistributorApplicant> webDistributorApplicantList = new ArrayList<>();
        for (Applicant applicant : applicantList) {
            Set<Application> applicationSet = applicant.getApplications();
            List<WebDistributorApplication> webDistributorApplicationList =
                    createWebDistributorApplications(applicationSet);
            int finalHours = 0;
            for (Application application : applicationSet) {
                if (application.getModule().equals(module)) {
                    finalHours = application.getFinalHours();
                }
            }
            WebDistributorApplicant webDistributorApplicant = WebDistributorApplicant.builder()
                    .username(applicant.getUniserial())
                    .webDistributorApplications(webDistributorApplicationList)
                    .distributorHours(finalHours + "")
                    .build();
            webDistributorApplicantList.add(webDistributorApplicant);
        }
        return webDistributorApplicantList;
    }

    private List<WebDistributorApplication> createWebDistributorApplications(final Set<Application> applicationSet) {
        List<WebDistributorApplication> webDistributorApplicationList = new ArrayList<>();
        for (Application application : applicationSet) {
            Evaluation evaluation = evaluationService.findByApplication(application);
            WebDistributorApplication webDistributorApplication = WebDistributorApplication.builder()
                    .applicantPriority(application.getPriority() + "")
                    .minHours(application.getMinHours() + "")
                    .maxHours(application.getMaxHours() + "")
                    .module(application.getModule())
                    .organizerHours(evaluation.getHours() + "")
                    .organizerPriority(evaluation.getPriority() + "")
                    .build();
            webDistributorApplicationList.add(webDistributorApplication);
        }
        return  webDistributorApplicationList;
    }
}
