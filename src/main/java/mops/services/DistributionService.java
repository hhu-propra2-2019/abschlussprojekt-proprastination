package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.model.classes.webclasses.WebDistributorApplication;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DistributionService {

    private final DistributionRepository distributionRepository;
    private final ModuleService moduleService;
    private final ApplicantService applicantService;
    private final EvaluationService evaluationService;

    /**
     * Injects Services and repositories
     * @param distributionRepository the injected repository
     * @param moduleService the services that manages modules
     * @param applicantService the services that manages applicants
     * @param evaluationService the services that manage evaluations
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DistributionService(final DistributionRepository distributionRepository,
                               final ModuleService moduleService,
                               final ApplicantService applicantService,
                               final EvaluationService evaluationService) {
        this.distributionRepository = distributionRepository;
        this.moduleService = moduleService;
        this.applicantService = applicantService;
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
            final List<Applicant> applicantList, String module) {
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
