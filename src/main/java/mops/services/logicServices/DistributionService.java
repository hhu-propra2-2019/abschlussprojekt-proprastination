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

    private static final int NUMBER_OF_ORGA_PRIOS = 4;
    private static final int NUMBER_OF_APPL_PRIO = 3;
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
     * distributes the Applicants
     */
    public void distribute() {
        final int[] hours = {7, 9, 17};

        List<Module> modules = moduleService.getModules();
        List<Applicant> allApplicants = applicantService.findAll();

        for (Applicant applicant : allApplicants) {
            saveChecked(applicant.getId() + "", "false");
            saveCollapsed(applicant.getId() + "", "false");
        }

        List<Applicant>[] applicantsPerModule = new List[modules.size()];

        for (int i = 0; i < modules.size(); i++) {
            applicantsPerModule[i] = new LinkedList<>();
        }

        int[][] maxHoursPerModule = new int[modules.size()][hours.length];
        int[][] countHoursPerModule = new int[modules.size()][hours.length];

        int moduleCount = 0;

        dbDistributionService.deleteAll();


        for (Module module : modules) {

            assignPerfectMatches(hours, allApplicants, applicantsPerModule,
                    maxHoursPerModule, countHoursPerModule, moduleCount, module);
            moduleCount++;
        }

        moduleCount = 0;

        for (Module module : modules) {

            assingLeftOverApplicants(hours, allApplicants, applicantsPerModule,
                    maxHoursPerModule, countHoursPerModule, moduleCount, module);
            dbDistributionService.save(Distribution.builder()
                    .employees(applicantsPerModule[moduleCount])
                    .module(module)
                    .build());
            moduleCount++;
        }
    }

    /**
     * assinges perfect matches to one module
     * @param hours
     * @param allApplicants
     * @param applicantsPerModule
     * @param maxHoursPerModule
     * @param countHoursPerModule
     * @param moduleCount
     * @param module
     */
    private void assignPerfectMatches(final int[] hours,
                                      final List<Applicant> allApplicants,
                                      final List<Applicant>[] applicantsPerModule,
                                      final int[][] maxHoursPerModule,
                                      final int[][] countHoursPerModule,
                                      final int moduleCount,
                                      final Module module) {
        List<Evaluation> evaluations = prepareEvaluationsList(allApplicants, module);

        List<Evaluation> sortedByOrgaPrio = new LinkedList<>();

        for (Evaluation evaluation : evaluations) {
            if ((evaluation.getPriority().getValue() + evaluation.getApplication().getPriority().getValue()) == 2) {
                sortedByOrgaPrio.add(evaluation);
            }
        }

        for (int i = 0; i < hours.length; i++) {
            maxHoursPerModule[moduleCount][i] = 0;
        }

        maxHoursPerModule[moduleCount][0] = Integer.parseInt(module.getSevenHourLimit());
        maxHoursPerModule[moduleCount][1] = Integer.parseInt(module.getNineHourLimit());
        maxHoursPerModule[moduleCount][2] = Integer.parseInt(module.getSeventeenHourLimit());

        for (Evaluation evaluation : sortedByOrgaPrio) {

            if (checkIfModuleHasSpace(hours.length,
                    maxHoursPerModule[moduleCount],
                    countHoursPerModule[moduleCount])) {

                Applicant applicant = applicantService.findByApplications(evaluation.getApplication());

                for (int i = 0; i < hours.length; i++) {

                    if (evaluation.getHours() == hours[i]
                            && countHoursPerModule[moduleCount][i] < maxHoursPerModule[moduleCount][i]) {
                        applicantsPerModule[moduleCount].add(applicant);
                        saveChecked(applicant.getId() + "", "true");
                        allApplicants.remove(applicant);
                        countHoursPerModule[moduleCount][i]++;
                    }
                }
            }
        }
    }

    /**
     * assinges from leftover Applicants to given module
     * @param hours
     * @param allApplicants
     * @param applicantsPerModule
     * @param maxHoursPerModule
     * @param countHoursPerModule
     * @param moduleCount
     * @param module
     */
    private void assingLeftOverApplicants(final int[] hours,
                                          final List<Applicant> allApplicants,
                                          final List<Applicant>[] applicantsPerModule,
                                          final int[][] maxHoursPerModule,
                                          final int[][] countHoursPerModule,
                                          final int moduleCount,
                                          final Module module) {
        List<Evaluation> evaluations = prepareEvaluationsList(allApplicants, module);

        List<Evaluation>[] sortedByOrgaPrio = new List[NUMBER_OF_ORGA_PRIOS];

        for (int i = 0; i < NUMBER_OF_ORGA_PRIOS; i++) {
            sortedByOrgaPrio[i] = new LinkedList<>();
        }

        for (Evaluation evaluation : evaluations) {
            sortedByOrgaPrio[evaluation.getPriority().getValue() - 1].add(evaluation);
        }

        for (int i = 0; i < NUMBER_OF_ORGA_PRIOS; i++) {
            sortedByOrgaPrio[i].sort(Comparator.comparing(a -> a.getApplication().getPriority().getValue()));
        }

        for (int x = 0; x < NUMBER_OF_ORGA_PRIOS - 1; x++) {

            if (checkIfModuleHasSpace(hours.length,
                    maxHoursPerModule[moduleCount],
                    countHoursPerModule[moduleCount])) {

                for (Evaluation evaluation : sortedByOrgaPrio[x]) {

                    Applicant applicant = applicantService.findByApplications(evaluation.getApplication());

                    for (int i = 0; i < hours.length; i++) {
                        if (evaluation.getHours() == hours[i]
                                && countHoursPerModule[moduleCount][i] < maxHoursPerModule[moduleCount][i]) {
                            applicantsPerModule[moduleCount].add(applicant);
                            allApplicants.remove(applicant);
                            countHoursPerModule[moduleCount][i]++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if module has space for more applicants
     * @param numberOfHours
     * @param maxHoursPerModule
     * @param countHoursPerModule
     * @return true if theres space
     */
    private boolean checkIfModuleHasSpace(final int numberOfHours,
                                          final int[] maxHoursPerModule,
                                          final int[] countHoursPerModule) {

        int allocatedStudents = 0;
        int sumOfStudentsToAllocate = 0;

        for (int i = 0; i < numberOfHours; i++) {
            allocatedStudents += countHoursPerModule[i];
        }
        for (int i = 0; i < numberOfHours; i++) {
            sumOfStudentsToAllocate += maxHoursPerModule[i];
        }

        return allocatedStudents != sumOfStudentsToAllocate;
    }

    /**
     * Prepare lists evaluation by Module
     * @param allApplicants
     * @param module
     * @return List of evalutaions
     */
    private List<Evaluation> prepareEvaluationsList(final List<Applicant> allApplicants, final Module module) {
        List<Evaluation> evaluations = new LinkedList<>();
        List<Application> preApplications = applicationService.findApplicationsByModule(module);
        List<Application> applications = new LinkedList<>();

        for (Application application : preApplications) {
            if (allApplicants.contains(applicantService.findByApplications(application))) {
                applications.add(application);
            }
        }

        for (Application application : applications) {
            Evaluation evaluation = evaluationService.findByApplication(application);
            evaluations.add(evaluation);
        }
        return evaluations;
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
        for (Application application : applicant.getApplications()) {
            if ("-1".equals(distributionId)) {
                for (Distribution distribution : dbDistributionService.findAll()) {
                    distribution.getEmployees().remove(applicant);
                    dbDistributionService.save(distribution);
                }
            } else {
                if (newDistribution.isPresent()) {
                    if (application.getModule().equals(newDistribution.get().getModule())) {
                        for (Distribution distribution : dbDistributionService.findAll()) {
                            distribution.getEmployees().remove(applicant);
                            dbDistributionService.save(distribution);
                        }
                        newDistribution.get().getEmployees().add(applicant);
                        dbDistributionService.save(newDistribution.get());
                    }
                }
            }
        }
    }

    /**
     * Sorts the WebDistributorApplicants by Matches
     * @param applicantList List with all WebDistributorApplicants for Distribution
     * @param module module of distribution
     * @return sorted List of Applicants
     */
    public List<WebDistributorApplicant> sort(final List<WebDistributorApplicant> applicantList, final String module) {
        List<WebDistributorApplicant> sortedApplicants = new LinkedList<>();
        LinkedList<WebDistributorApplicant>[][] orgaPrios = new LinkedList[NUMBER_OF_ORGA_PRIOS][NUMBER_OF_APPL_PRIO];
        LinkedList<WebDistributorApplicant> wrongApplicants = new LinkedList<>();
        wrongApplicants.addAll(applicantList);
        for (int i = 0; i < NUMBER_OF_ORGA_PRIOS; i++) {
            for (int j = 0; j < NUMBER_OF_APPL_PRIO; j++) {
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
        for (int i = 0; i < NUMBER_OF_ORGA_PRIOS; i++) {
            for (int j = 0; j < NUMBER_OF_APPL_PRIO; j++) {
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
     * saves collapsed status that distributor sets
     * @param applicantId applicantId
     * @param collapsed collapsed
     */
    public void saveCollapsed(final String applicantId, final String collapsed) {
        Applicant applicant = applicantService.findById(Long.parseLong(applicantId));
        boolean collapsedBoolean = Boolean.parseBoolean(collapsed);
        applicantService.saveApplicant(applicant.toBuilder()
                .collapsed(collapsedBoolean)
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
