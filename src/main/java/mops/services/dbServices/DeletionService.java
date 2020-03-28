package mops.services.dbServices;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.repositories.ApplicantRepository;
import mops.repositories.ApplicationRepository;
import mops.repositories.DistributionRepository;
import mops.repositories.EvaluationRepository;
import mops.repositories.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class DeletionService {

    private Logger logger = LoggerFactory.getLogger(DeletionService.class);

    private ApplicationRepository applicationRepository;
    private ApplicantRepository applicantRepository;
    private ModuleRepository moduleRepository;
    private DistributionRepository distributionRepository;
    private EvaluationRepository evaluationRepository;


    /**
     * Constructor
     *
     * @param applicationRepository  repo
     * @param applicantRepository    repo
     * @param moduleRepository       repo
     * @param distributionRepository repo
     * @param evaluationRepository   repo
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DeletionService(final ApplicationRepository applicationRepository,
                           final ApplicantRepository applicantRepository,
                           final ModuleRepository moduleRepository,
                           final DistributionRepository distributionRepository,
                           final EvaluationRepository evaluationRepository) {
        this.applicationRepository = applicationRepository;
        this.applicantRepository = applicantRepository;
        this.moduleRepository = moduleRepository;
        this.distributionRepository = distributionRepository;
        this.evaluationRepository = evaluationRepository;
    }

    /**
     * Deletes Applicant given its uniserial.
     *
     * @param uniserial Unique uniserial
     * @param account   keycloak account
     * @return Approval Message.
     */
    public String deleteApplicant(final String uniserial, final Account account) {
        Applicant applicant = applicantRepository.findByUniserial(uniserial);
        applicantRepository.deleteById(applicant.getId());
        for (Application application : applicant.getApplications()) {
            applicationRepository.deleteById(application.getId());
        }
        logger.warn(account.getName() + " Deleted Applicant with ID: " + applicant.getId() + ", Uniserial: "
                + applicant.getUniserial());
        return "Der Bewerber mit der Kennung: " + uniserial + " wurde inkl. aller Bewerbungen gelöscht.";
    }

    /**
     * Deletes Module and its Distribution by modulename
     *
     * @param modulename module name
     * @param account    Keycloak account
     * @return Approval Message.
     */
    public String deleteModule(final String modulename, final Account account) {
        Module module = moduleRepository.findDistinctByName(modulename);

        logger.info(account.getName() + ": Deleting Module with ID: " + module.getId() + " | " + module.getName());
        moduleRepository.deleteById(module.getId());
        logger.warn(account.getName() + ": Deleted module: " + module.getName()
                + " and all matching evaluations/distributions/applications.");
        return "Das Modul: " + modulename + " wurde inkl. aller Bewerbungen gelöscht.";
    }

    /**
     * Deletes given Application and writes into logger.
     *
     * @param id      application id;
     * @param account keycloak account.
     * @return Approval Message.
     */
    public String deleteApplication(final long id, final Account account) {
        applicationRepository.deleteById(id);
        logger.warn(account.getName() + ": Deleted Application: " + id
                + " and all matching evaluations/distributions/applications.");
        return "Die Bewerbung mit der ID: " + id + " wurde gelöscht.";
    }

    /**
     * Deletes all data.
     *
     * @param account keycloak account
     * @return Approval Message.
     */
    public String deleteAll(final Account account) {
        logger.warn("Database deletion initiated!");
        applicantRepository.deleteAll();
        moduleRepository.deleteAll();
        distributionRepository.deleteAll();
        evaluationRepository.deleteAll();
        logger.warn(account.getName() + " Wiped the Database.");
        return "Alle Daten wurden erfolgreich gelöscht";
    }
}
