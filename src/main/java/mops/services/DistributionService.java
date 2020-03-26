package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Application.ApplicationBuilder;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.model.classes.webclasses.WebDistributorApplication;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DistributionService {

    private DistributionRepository distributionRepository;
    private ModuleService moduleService;
    private ApplicantService applicantService;
    private ApplicationService applicationService;
    private EvaluationService evaluationService;

    /**
     * Injects Services and repositories
     *
     * @param distributionRepository the injected repository
     * @param moduleService          the services that manages modules
     * @param applicantService       the services that manages applicants
     * @param applicationService     the services that manages applications
     * @param evaluationService      the services that manages evaluations
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
    }

    /**
     * Setup to init data.
     */
    @PostConstruct
    public void setup() {
        distributionRepository.deleteAll();
    }

    /**
     * Dummy functions that assignes applicants to Distributions
     */
    public void assign() {
        distributionRepository.save(Distribution.builder()
                .employees(applicantService.findAll())
                .build());
    }

    /**
     * distributes the Applicants
     */
    public void distribute() {
        final int numberOfPriorities = 4;
        final int numberOfHours = 3;
        final int[] hours = {7, 9, 17};
        List<Module> modules = moduleService.getModules();
        List<Applicant> allApplicants = applicantService.findAll();
        List<Applicant>[] applicantsPerModule = new List[modules.size()];
        for (int i = 0; i < modules.size(); i++) {
            applicantsPerModule[i] = new LinkedList<>();
        }
        int[][] maxHoursPerModule = new int[modules.size()][numberOfHours];
        int[][] countHoursPerModule = new int[modules.size()][numberOfHours];
        int moduleCount = 0;
        distributionRepository.deleteAll();
        for (Module module : modules) {
            List<Evaluation> evaluations = new LinkedList<>();
            List<Application> preApplications = applicationService.findApplicationsByModule(module);
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

            List<Evaluation> sortedByOrgaPrio = new LinkedList<>();

            for (Evaluation evaluation : evaluations) {
                if ((evaluation.getPriority().getValue() + evaluation.getApplication().getPriority().getValue()) == 2) {
                    sortedByOrgaPrio.add(evaluation);
                }
            }

            for (int i = 0; i < numberOfHours; i++) {
                maxHoursPerModule[moduleCount][i] = 0;
            }

            maxHoursPerModule[moduleCount][0] = Integer.parseInt(module.getSevenHourLimit());
            maxHoursPerModule[moduleCount][1] = Integer.parseInt(module.getNineHourLimit());
            maxHoursPerModule[moduleCount][2] = Integer.parseInt(module.getSeventeenHourLimit());

            for (Evaluation evaluation : sortedByOrgaPrio) {
                int countSum = 0;
                int maxSum = 0;
                for (int i = 0; i < numberOfHours; i++) {
                    countSum += countHoursPerModule[moduleCount][i];
                }
                for (int i = 0; i < numberOfHours; i++) {
                    maxSum += maxHoursPerModule[moduleCount][i];
                }

                if (countSum == maxSum) {
                    break;
                }

                Applicant applicant = applicantService.findByApplications(evaluation.getApplication());
                for (int i = 0; i < numberOfHours; i++) {
                    if (evaluation.getHours() == hours[i]
                            && countHoursPerModule[moduleCount][i] < maxHoursPerModule[moduleCount][i]) {
                        applicantsPerModule[moduleCount].add(applicant);
                        saveChecked(applicant.getId() + "", "true");
                        allApplicants.remove(applicant);
                        countHoursPerModule[moduleCount][i]++;
                    }
                }
            }
            moduleCount++;
        }

        moduleCount = 0;

        for (Module module : modules) {
            List<Evaluation> evaluations = new LinkedList<>();
            List<Application> preApplications = applicationService.findApplicationsByModule(module);
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
                sortedByOrgaPrio[evaluation.getPriority().getValue() - 1].add(evaluation);
            }

            for (int i = 0; i < numberOfPriorities; i++) {
                sortedByOrgaPrio[i].sort(Comparator.comparing(a -> a.getApplication().getPriority().getValue()));
            }

            for (int x = 0; x < numberOfPriorities; x++) {
                int countSum = 0;
                int maxSum = 0;
                for (int i = 0; i < numberOfHours; i++) {
                    countSum += countHoursPerModule[moduleCount][i];
                }
                for (int i = 0; i < numberOfHours; i++) {
                    maxSum += maxHoursPerModule[moduleCount][i];
                }
                if (countSum == maxSum) {
                    break;
                }

                for (Evaluation evaluation : sortedByOrgaPrio[x]) {

                    for (int i = 0; i < numberOfHours; i++) {
                        countSum += countHoursPerModule[moduleCount][i];
                    }
                    for (int i = 0; i < numberOfHours; i++) {
                        maxSum += maxHoursPerModule[moduleCount][i];
                    }

                    if (countSum == maxSum) {
                        break;
                    }

                    Applicant applicant = applicantService.findByApplications(evaluation.getApplication());
                    for (int i = 0; i < numberOfHours; i++) {
                        if (evaluation.getHours() == hours[i]
                                && countHoursPerModule[moduleCount][i] < maxHoursPerModule[moduleCount][i]) {
                            applicantsPerModule[moduleCount].add(applicant);
                            allApplicants.remove(applicant);
                            countHoursPerModule[moduleCount][i]++;
                        }
                    }
                }
            }
            distributionRepository.save(Distribution.builder()
                    .employees(applicantsPerModule[moduleCount])
                    .module(module)
                    .build());
            moduleCount++;
        }
    }

    /**
     * changes finalHours in application
     *
     * @param evaluation eval
     */
    private void changeFinalHours(final Evaluation evaluation) {
        ApplicationBuilder applicationBuilder = evaluation.getApplication().toBuilder();
        Application application = applicationBuilder.finalHours(evaluation.getHours()).build();
        applicationService.save(application);
    }


    /**
     * changes all FinalHours to the organizers wish
     */
    public void changeAllFinalHours() {
        List<Application> applications = applicationService.findAll();
        for (Application application : applications) {
            Evaluation evaluation = evaluationService.findByApplication(application);
            changeFinalHours(evaluation);
        }
    }

    /**
     * Finds the Distribution of a model
     *
     * @param module the model
     * @return List of Distributions
     */
    public Distribution findByModule(final Module module) {
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
     * Converts Distributions to Web Distributions
     * @return List of WebDistributions
     */

    public List<WebDistribution> convertDistributionsToWebDistributions() {
        List<WebDistribution> webDistributionList = new ArrayList<>();
        List<Distribution> distributionList = findAll();
        for (Distribution distribution : distributionList) {
            List<WebDistributorApplicant> webDistributorApplicantList =
                    convertApplicantToWebDistributorApplicant(distribution.getEmployees(), distribution.getModule());
            WebDistribution webDistribution = WebDistribution.builder()
                    .module(distribution.getModule().getName())
                    .id(distribution.getId() + "")
                    .hours7(distribution.getModule().getSevenHourLimit())
                    .hours9(distribution.getModule().getNineHourLimit())
                    .hours17(distribution.getModule().getSeventeenHourLimit())
                    .webDistributorApplicants(sort(webDistributorApplicantList, distribution.getModule().getName()))
                    .build();
            webDistributionList.add(webDistribution);
        }
        List<WebDistributorApplicant> webDistributorApplicantList =
                convertUnassignedApplicantsToWebDistributorApplicants(findAllUnassigned());
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
                    .type(getTypeOfApplicant(applicant))
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
                    .type(getTypeOfApplicant(applicant))
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

    private List<Applicant> findAllUnassigned() {
        List<Applicant> allApplicants = applicantService.findAll();
        List<Distribution> allDistributions = findAll();
        List<Applicant> distributedApplicants = new LinkedList<>();

        for (Distribution distribution : allDistributions) {
            distributedApplicants.addAll(distribution.getEmployees());
        }
        allApplicants.removeIf(distributedApplicants::contains);
        return allApplicants;
    }

    /**
     * moves an applicant to other distribution
     * @param applicantId the id of the applicant being moved
     * @param distributionId the id of the new distribution
     */
    public void moveApplicant(final String applicantId, final String distributionId) {
        Optional<Distribution> newDistribution = distributionRepository.findById(Long.parseLong(distributionId));
        Applicant applicant = applicantService.findById(Long.parseLong(applicantId));
        if (newDistribution.isPresent()) {
            for (Distribution distribution : distributionRepository.findAll()) {
                distribution.getEmployees().remove(applicant);
                distributionRepository.save(distribution);
            }
            newDistribution.get().getEmployees().add(applicant);
            distributionRepository.save(newDistribution.get());
        }
    }

    /**
     * AHHHHHHHHHHHHHHHHHHHHHHH
     * @param applicantId
     * @param distributionId
     */
    public void addApplicant(final String applicantId, final String distributionId) {
        Optional<Distribution> newDistribution = distributionRepository.findById(Long.parseLong(distributionId));
        Applicant applicant = applicantService.findById(Long.parseLong(applicantId));
        if (newDistribution.isPresent()) {
            distributionRepository.save(newDistribution.get());
            newDistribution.get().getEmployees().add(applicant);
            distributionRepository.save(newDistribution.get());
        }
    }

    /**
     * Sorts the WebDistributorApplicants by Matches
     * @param applicantList List with all WebDistributorApplicants for Distribution
     * @param module module of distribution
     * @return sortet List of Applicants
     */
    private List<WebDistributorApplicant> sort(final List<WebDistributorApplicant> applicantList, final String module) {
        final int numberOfOrgaPrios = 4;
        final int numberOfApplPrio = 3;
        List<WebDistributorApplicant> sortedApplicants = new LinkedList<>();
        LinkedList<WebDistributorApplicant>[][] orgaPrios = new LinkedList[numberOfOrgaPrios][numberOfApplPrio];
        LinkedList<WebDistributorApplicant> wrongApplicants = new LinkedList<>();
        wrongApplicants.addAll(applicantList);
        for (int i = 0; i < numberOfOrgaPrios; i++) {
            for (int j = 0; j < numberOfApplPrio; j++) {
                orgaPrios[i][j] = new LinkedList<>();
            }
        }
        for (WebDistributorApplicant applicant : applicantList) {
            for (WebDistributorApplication application : applicant.getWebDistributorApplications()) {
                if (module.equals(application.getModule())) {
                    int orgaPrio = application.getOrganizerPriority().getValue();
                    int applPrio = application.getApplicantPriority().getValue();
                    orgaPrios[orgaPrio - 1][applPrio - 1].add(applicant);
                    wrongApplicants.remove(applicant);
                }
            }
        }
        for (int i = 0; i < numberOfOrgaPrios; i++) {
            for (int j = 0; j < numberOfApplPrio; j++) {
                sortedApplicants.addAll(orgaPrios[i][j]);
            }
        }
        sortedApplicants.addAll(wrongApplicants);
        return sortedApplicants;
    }

    private String getTypeOfApplicant(final Applicant applicant) {
        if ("Keins".equals(applicant.getCerts().getName())) {
            return "SHK";
        } else {
            return "WHB";
        }
    }

    /**
     * saves new set hours
     * @param applicantId applicantId
     * @param distributionId distributionId
     * @param hours hours
     */
    public void saveHours(final String applicantId, final String distributionId, final String hours) {
        Applicant applicant = applicantService.findById(Long.parseLong(applicantId));
        Optional<Distribution> distribution = distributionRepository.findById(Long.parseLong(distributionId));
        if (distribution.isPresent()) {
            for (Application application : applicant.getApplications()) {
                if (application.getModule().equals(distribution.get().getModule())) {
                    applicationService.save(application.toBuilder()
                            .finalHours(Integer.parseInt(hours))
                            .build());
                }

            }
        }
    }

    /**
     * saves ckecks that distributor sets
     * @param applicantId applicantId
     * @param checked checked
     */
    public void saveChecked(final String applicantId, final String checked) {
        Applicant applicant = applicantService.findById(Long.parseLong(applicantId));
        boolean checkedBoolean = Boolean.parseBoolean(checked);
        applicantService.saveApplicant(applicant.toBuilder()
                .checked(checkedBoolean)
                .build());
    }

    /**
     * Returns number of distribution entrys. Faster than getting all first.
     *
     * @return long number.
     */
    public long getSize() {
        return distributionRepository.count();
    }
}
