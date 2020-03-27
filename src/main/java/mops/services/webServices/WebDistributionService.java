package mops.services.webServices;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.model.classes.webclasses.WebDistributorApplication;
import mops.services.dbServices.DbDistributionService;
import mops.services.dbServices.EvaluationService;
import mops.services.logicServices.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WebDistributionService {

    private final DbDistributionService dbDistributionService;
    private final DistributionService distributionService;
    private final EvaluationService evaluationService;

    public WebDistributionService(DbDistributionService dbDistributionService, DistributionService distributionService, EvaluationService evaluationService) {
        this.dbDistributionService = dbDistributionService;
        this.distributionService = distributionService;
        this.evaluationService = evaluationService;
    }

    /**
     * Converts Distributions to Web Distributions
     * @return List of WebDistributions
     */
    public List<WebDistribution> convertDistributionsToWebDistributions() {
        List<WebDistribution> webDistributionList = new ArrayList<>();
        List<Distribution> distributionList = dbDistributionService.findAll();
        for (Distribution distribution : distributionList) {
            List<WebDistributorApplicant> webDistributorApplicantList =
                    convertApplicantToWebDistributorApplicant(distribution.getEmployees(), distribution.getModule());
            WebDistribution webDistribution = WebDistribution.builder()
                    .module(distribution.getModule().getName())
                    .id(distribution.getId() + "")
                    .hours7(distribution.getModule().getSevenHourLimit())
                    .hours9(distribution.getModule().getNineHourLimit())
                    .hours17(distribution.getModule().getSeventeenHourLimit())
                    .webDistributorApplicants(distributionService.sort(webDistributorApplicantList, distribution.getModule().getName()))
                    .build();
            webDistributionList.add(webDistribution);
        }
        List<WebDistributorApplicant> webDistributorApplicantList =
                convertUnassignedApplicantsToWebDistributorApplicants(distributionService.findAllUnassigned());
        WebDistribution webDistribution = WebDistribution.builder()
                .module("Nicht Zugeteilt")
                .hours7("0")
                .hours9("0")
                .hours17("0")
                .id(-1 + "")
                .webDistributorApplicants(webDistributorApplicantList)
                .build();
        webDistributionList.add(webDistribution);
        return webDistributionList;
    }

    private List<WebDistributorApplicant> convertUnassignedApplicantsToWebDistributorApplicants(
            final List<Applicant> applicants) {
        List<WebDistributorApplicant> webDistributorApplicantList = new ArrayList<>();
        for (Applicant applicant : applicants) {
            Set<Application> applicationSet = applicant.getApplications();
            List<WebDistributorApplication> webDistributorApplicationList =
                    createWebDistributorApplications(applicationSet);
            WebDistributorApplicant webDistributorApplicant = WebDistributorApplicant.builder()
                    .username(applicant.getUniserial())
                    .id(applicant.getId() + "")
                    .type(distributionService.getTypeOfApplicant(applicant))
                    .checked(applicant.isChecked())
                    .fullName(applicant.getFirstName() + " " + applicant.getSurname())
                    .webDistributorApplications(webDistributorApplicationList)
                    .distributorHours("0")
                    .build();
            webDistributorApplicantList.add(webDistributorApplicant);
        }
        return webDistributorApplicantList;
    }

    private List<WebDistributorApplicant> convertApplicantToWebDistributorApplicant(
            final List<Applicant> applicantList, final Module module) {
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
                    .id(applicant.getId() + "")
                    .type(distributionService.getTypeOfApplicant(applicant))
                    .checked(applicant.isChecked())
                    .fullName(applicant.getFirstName() + " " + applicant.getSurname())
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
                    .applicantPriority(application.getPriority())
                    .minHours(application.getMinHours() + "")
                    .maxHours(application.getMaxHours() + "")
                    .module(application.getModule().getName())
                    .moduleShort(application.getModule().getShortName())
                    .organizerHours(evaluation.getHours() + "")
                    .organizerPriority(evaluation.getPriority())
                    .build();
            webDistributorApplicationList.add(webDistributorApplication);
        }
        return  webDistributorApplicationList;
    }
}
