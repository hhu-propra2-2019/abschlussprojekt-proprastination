package mops.services.dbServices;

import mops.model.classes.Applicant;
import mops.model.classes.Applicant.ApplicantBuilder;
import mops.model.classes.Application;
import mops.repositories.ApplicantRepository;
import mops.services.PDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@EnableAutoConfiguration
public class ApplicantService {

    private Logger logger = LoggerFactory.getLogger(PDFService.class);

    private final ApplicantRepository applicantRepository;

    /**
     * Lets Spring inject the Repository
     *
     * @param applicantRepository the injected Repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ApplicantService(final ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    /**
     * Saves or Updates Applicant to Repository
     *
     * @param applicant the new Applicant
     */
    public void saveApplicant(final Applicant applicant) {
        applicantRepository.save(applicant);
    }

    /**
     * Finds all Applicants
     *
     * @return List of Applicants
     */
    public List<Applicant> findAll() {
        return applicantRepository.findAll();
    }

    /**
     * Finds first applicant with give uniserial
     * uniserial should be unique, so only the
     * first result is returned
     *
     * @param uniserial Unikennung
     * @return the Applicant found
     */
    public Applicant findByUniserial(final String uniserial) {
        return applicantRepository.findByUniserial(uniserial);
    }

    /**
     * Deletes Application from Applicant and persists it.
     *
     * @param application Application.
     * @param applicant   Applicant.
     */
    public void deleteApplication(final Application application, final Applicant applicant) {
        Set<Application> applications = applicant.getApplications();
        ArrayList<Application> list = new ArrayList<>(applications);
        list.remove(application);
        applications = new HashSet<>(list);
        ApplicantBuilder applicantBuilder = applicant.toBuilder();
        Applicant newApplicant = applicantBuilder.clearApplications().applications(applications).build();
        applicantRepository.save(newApplicant);
    }

/*    /**
     * Returns a Set of all Modules the Applicant has not submitted an application yet.
     *
     * @param applicant Applicant.
     * @param modules   all Modules
     * @return Set of Modules.
     */
  /*  public List<Module> getAllNotfilledModules(final Applicant applicant, final List<Module> modules) {
        for (Application app : applicant.getApplications()) {
            modules.remove(app.getModule());
        }
        return modules;
    }*/

    /**
     * Finds the corrosponding applicant to the application
     * @param application the application
     * @return the applicant
     */
    public Applicant findByApplications(final Application application) {
        Optional<Applicant> applicant = applicantRepository.findByApplications(application);
        if (applicant.isEmpty()) {
            logger.error("Empty Applicant for Application" + application);
            return null;
        }
        return applicant.get();
    }

    /**
     * Delete all.
     */
    public void deleteAll() {
        applicantRepository.deleteAll();
    }

    /**
     * find Applicant by ID
     * @param applicantId id
     * @return applicant
     */
    public Applicant findById(final long applicantId) {
        return applicantRepository.findById(applicantId);
    }
}
