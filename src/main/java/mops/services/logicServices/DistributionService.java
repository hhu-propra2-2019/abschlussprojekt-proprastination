package mops.services.logicServices;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Application.ApplicationBuilder;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.model.classes.webclasses.WebDistributorApplication;
import mops.services.dbServices.DbDistributionService;
import mops.services.dbServices.EvaluationService;
import mops.services.dbServices.ModuleService;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class DistributionService {

    private DbDistributionService dbDistributionService;
    private ModuleService moduleService;
    private ApplicantService applicantService;
    private ApplicationService applicationService;
    private EvaluationService evaluationService;

    /**
     * Injects Services and repositories
     *
     * @param dbDistributionService the injected service
     * @param moduleService          the services that manages modules
     * @param applicantService       the services that manages applicants
     * @param applicationService     the services that manages applications
     * @param evaluationService      the services that manages evaluations
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DistributionService(final DbDistributionService dbDistributionService,
                               final ModuleService moduleService,
                               final ApplicantService applicantService,
                               final ApplicationService applicationService,
                               final EvaluationService evaluationService) {
        this.dbDistributionService = dbDistributionService;
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
        dbDistributionService.deleteAll();
    }

    /**
     * Dummy functions that assignes applicants to Distributions
     */
    public void assign() {
        dbDistributionService.save(Distribution.builder()
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
        dbDistributionService.deleteAll();
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
            dbDistributionService.save(Distribution.builder()
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
     * -
     * @return -
     */
    public List<Applicant> findAllUnassigned() {
        List<Applicant> allApplicants = applicantService.findAll();
        List<Distribution> allDistributions = dbDistributionService.findAll();
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
        Optional<Distribution> newDistribution = dbDistributionService.findById(Long.parseLong(distributionId));
        Applicant applicant = applicantService.findById(Long.parseLong(applicantId));
        if (newDistribution.isPresent()) {
            for (Distribution distribution : dbDistributionService.findAll()) {
                distribution.getEmployees().remove(applicant);
                dbDistributionService.save(distribution);
            }
            newDistribution.get().getEmployees().add(applicant);
            dbDistributionService.save(newDistribution.get());
        }
    }

    /**
     * Sorts the WebDistributorApplicants by Matches
     * @param applicantList List with all WebDistributorApplicants for Distribution
     * @param module module of distribution
     * @return sortet List of Applicants
     */
    public List<WebDistributorApplicant> sort(final List<WebDistributorApplicant> applicantList, final String module) {
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

    /**
     * -
     * @param applicant
     * @return -
     */
    public String getTypeOfApplicant(final Applicant applicant) {
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
        Optional<Distribution> distribution = dbDistributionService.findById(Long.parseLong(distributionId));
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
        return dbDistributionService.count();
    }
}
