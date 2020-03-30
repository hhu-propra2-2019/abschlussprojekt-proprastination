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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WebDistributionService {

    private final DbDistributionService dbDistributionService;
    private final DistributionService distributionService;
    private final EvaluationService evaluationService;

    /**
     * Constructor
     * @param dbDistributionService
     * @param distributionService
     * @param evaluationService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebDistributionService(final DbDistributionService dbDistributionService,
                                  final DistributionService distributionService,
                                  final EvaluationService evaluationService) {
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
            int[] hoursSet = getCountOfApplicantsPerHour(distribution);
            WebDistribution webDistribution = WebDistribution.builder()
                    .module(distribution.getModule().getName())
                    .id(distribution.getId() + "")
                    .hours7(distribution.getModule().getSevenHourLimit())
                    .hours9(distribution.getModule().getNineHourLimit())
                    .hours17(distribution.getModule().getSeventeenHourLimit())
                    .hours7Set(hoursSet[0] + "")
                    .hours9Set(hoursSet[1] + "")
                    .hours17Set(hoursSet[2] + "")
                    .webDistributorApplicants(distributionService
                            .sort(webDistributorApplicantList, distribution.getModule().getName()))
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
                .hours7Set("0")
                .hours9Set("0")
                .hours17Set("0")
                .id(-1 + "")
                .webDistributorApplicants(webDistributorApplicantList)
                .build();
        webDistributionList.add(webDistribution);
        return webDistributionList;
    }

    private int[] getCountOfApplicantsPerHour(final Distribution distribution) {
        final int countOfHours = 3;
        final int hours7 = 7;
        final int hours9 = 9;
        final int hours17 = 17;
        int[] hoursSet = new int[countOfHours];
        for (Applicant applicant : distribution.getEmployees()) {
            int finalHours = getFinalHoursOfApplicant(applicant, distribution.getModule());
            switch (finalHours) {
                case hours7: hoursSet[0]++; break;
                case hours9: hoursSet[1]++; break;
                case hours17: hoursSet[2]++; break;
                default:
            }
        }
        return hoursSet;
    }

    private int getFinalHoursOfApplicant(final Applicant applicant, final Module module) {
        for (Application application : applicant.getApplications()) {
            if (module.equals(application.getModule())) {
                return application.getFinalHours();
            }
        }
        return 0;
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
                    .collapsed(applicant.isCollapsed())
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
                    .collapsed(applicant.isCollapsed())
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
