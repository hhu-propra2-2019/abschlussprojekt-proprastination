package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.model.classes.webclasses.WebDistributorApplication;
import mops.model.classes.Module;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
        List<Module> modules = moduleService.getModules();
        for (Module module : modules) {
            List<Evaluation> evaluations = new LinkedList<>();
            List<Application> applications = applicationService.findApplicationsByModule(module.getName());
            for (Application application : applications) {
                Evaluation evaluation = evaluationService.findByApplication(application);
                evaluations.add(evaluation);
            }

            List<Evaluation>[] sortedByOrgaPrio = new LinkedList[numberOfPriorities];

            for (int i = 0; i < numberOfPriorities; i++) {
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

            for (int i = 0; i < numberOfPriorities; i++) {
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

            for (int i = 0; i < numberOfPriorities; i++) {
                if (count7 == module.getMax7() && count9 == module.getMax9() && count17 == module.getMax17()) {
                    break;
                }
                for (Evaluation evaluation : sortedByOrgaPrio[i]) {
                    if (count7 == module.getMax7() && count9 == module.getMax9() && count17 == module.getMax17()) {
                        break;
                    }
                    if (evaluation.getHours() == sevenHours && count7 < module.getMax7()) {
                        Applicant oldApplicant = evaluation.getApplication().getApplicant();
                        Set<Application> newApplicationSet = oldApplicant.getApplications();
                        Application oldApplication = evaluation.getApplication();
                        newApplicationSet.remove(oldApplication);
                        newApplicationSet.add(Application.builder()
                                .id(oldApplication.getId())
                                .minHours(oldApplication.getMinHours())
                                .finalHours(evaluation.getHours())
                                .maxHours(oldApplication.getMaxHours())
                                .module(oldApplication.getModule())
                                .priority(oldApplication.getPriority())
                                .grade(oldApplication.getGrade())
                                .lecturer(oldApplication.getLecturer())
                                .semester(oldApplication.getSemester())
                                .role(oldApplication.getRole())
                                .comment(oldApplication.getComment())
                                .applicant(oldApplication.getApplicant())
                                .build());
                        applicantService.saveApplicant(Applicant.builder()
                                .applications(newApplicationSet)
                                .uniserial(oldApplicant.getUniserial())
                                .certs(oldApplicant.getCerts())
                                .status(oldApplicant.getStatus())
                                .course(oldApplicant.getCourse())
                                .nationality(oldApplicant.getComment())
                                .birthday(oldApplicant.getBirthday())
                                .address(oldApplicant.getAddress())
                                .birthplace(oldApplicant.getBirthplace())
                                .comment(oldApplicant.getComment())
                                .surname(oldApplicant.getSurname())
                                .firstName(oldApplicant.getFirstName())
                                .gender(oldApplicant.getGender())
                                .build());
                        distributedApplicants.add(evaluation.getApplication().getApplicant());
                        count7++;
                    }
                    if (evaluation.getHours() == nineHours && count7 < module.getMax9()) {
                        Applicant oldApplicant = evaluation.getApplication().getApplicant();
                        Set<Application> newApplicationSet = oldApplicant.getApplications();
                        Application oldApplication = evaluation.getApplication();
                        newApplicationSet.remove(oldApplication);
                        newApplicationSet.add(Application.builder()
                                .id(oldApplication.getId())
                                .minHours(oldApplication.getMinHours())
                                .finalHours(evaluation.getHours())
                                .maxHours(oldApplication.getMaxHours())
                                .module(oldApplication.getModule())
                                .priority(oldApplication.getPriority())
                                .grade(oldApplication.getGrade())
                                .lecturer(oldApplication.getLecturer())
                                .semester(oldApplication.getSemester())
                                .role(oldApplication.getRole())
                                .comment(oldApplication.getComment())
                                .applicant(oldApplication.getApplicant())
                                .build());
                        applicantService.saveApplicant(Applicant.builder()
                                .applications(newApplicationSet)
                                .uniserial(oldApplicant.getUniserial())
                                .certs(oldApplicant.getCerts())
                                .status(oldApplicant.getStatus())
                                .course(oldApplicant.getCourse())
                                .nationality(oldApplicant.getComment())
                                .birthday(oldApplicant.getBirthday())
                                .address(oldApplicant.getAddress())
                                .birthplace(oldApplicant.getBirthplace())
                                .comment(oldApplicant.getComment())
                                .surname(oldApplicant.getSurname())
                                .firstName(oldApplicant.getFirstName())
                                .gender(oldApplicant.getGender())
                                .build());
                        distributedApplicants.add(evaluation.getApplication().getApplicant());
                        count9++;
                    }
                    if (evaluation.getHours() == seventeenHours && count7 < module.getMax17()) {
                        Applicant oldApplicant = evaluation.getApplication().getApplicant();
                        Set<Application> newApplicationSet = oldApplicant.getApplications();
                        Application oldApplication = evaluation.getApplication();
                        newApplicationSet.remove(oldApplication);
                        newApplicationSet.add(Application.builder()
                                .id(oldApplication.getId())
                                .minHours(oldApplication.getMinHours())
                                .finalHours(evaluation.getHours())
                                .maxHours(oldApplication.getMaxHours())
                                .module(oldApplication.getModule())
                                .priority(oldApplication.getPriority())
                                .grade(oldApplication.getGrade())
                                .lecturer(oldApplication.getLecturer())
                                .semester(oldApplication.getSemester())
                                .role(oldApplication.getRole())
                                .comment(oldApplication.getComment())
                                .applicant(oldApplication.getApplicant())
                                .build());
                        applicantService.saveApplicant(Applicant.builder()
                                .applications(newApplicationSet)
                                .uniserial(oldApplicant.getUniserial())
                                .certs(oldApplicant.getCerts())
                                .status(oldApplicant.getStatus())
                                .course(oldApplicant.getCourse())
                                .nationality(oldApplicant.getComment())
                                .birthday(oldApplicant.getBirthday())
                                .address(oldApplicant.getAddress())
                                .birthplace(oldApplicant.getBirthplace())
                                .comment(oldApplicant.getComment())
                                .surname(oldApplicant.getSurname())
                                .firstName(oldApplicant.getFirstName())
                                .gender(oldApplicant.getGender())
                                .build());
                        distributedApplicants.add(evaluation.getApplication().getApplicant());
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

    /**
     * converts Distributions to Web Distributions
     * @return List of WebDistributions
     */

    public List<WebDistribution> convertDistributionsToWebDistributions() {
        List<WebDistribution> webDistributionList = new ArrayList<>();
        List<Distribution> distributionList = findAll();
        for (Distribution distribution : distributionList) {
            List<WebDistributorApplicant> webDistributorApplicantList =
                    convertApplicantToWebDistributorApplicant(distribution.getEmployees());
            WebDistribution webDistribution = WebDistribution.builder()
                    .module(distribution.getModule())
                    .webDistributorApplicants(webDistributorApplicantList)
                    .build();
            webDistributionList.add(webDistribution);
        }
        return webDistributionList;
    }

    private List<WebDistributorApplicant> convertApplicantToWebDistributorApplicant(
            final List<Applicant> applicantList) {
        List<WebDistributorApplicant> webDistributorApplicantList = new ArrayList<>();
        for (Applicant applicant : applicantList) {
            Set<Application> applicationSet = applicant.getApplications();
            List<WebDistributorApplication> webDistributorApplicationList =
                    createWebDistributorApplications(applicationSet);
            WebDistributorApplicant webDistributorApplicant = WebDistributorApplicant.builder()
                    .username(applicant.getUniserial())
                    .webDistributorApplications(webDistributorApplicationList)
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
