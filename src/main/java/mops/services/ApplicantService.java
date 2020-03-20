package mops.services;

import mops.model.classes.Applicant;
import mops.repositories.ApplicantRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@EnableAutoConfiguration
public class ApplicantService {

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
     * Updates Applicant without changing his applications
     *
     * @param newApplicant The Object containing the new information
     */
    public void updateApplicantWithoutChangingApplications(final Applicant newApplicant) {
        Applicant oldApplicant = findByUniserial(newApplicant.getUniserial());
        saveApplicant(Applicant.builder()
                .applications(oldApplicant.getApplications())
                .uniserial(newApplicant.getUniserial())
                .certs(newApplicant.getCerts())
                .status(newApplicant.getStatus())
                .course(newApplicant.getCourse())
                .nationality(newApplicant.getComment())
                .birthday(newApplicant.getBirthday())
                .address(newApplicant.getAddress())
                .birthplace(newApplicant.getBirthplace())
                .comment(newApplicant.getComment())
                .surname(newApplicant.getSurname())
                .firstName(newApplicant.getFirstName())
                .gender(newApplicant.getGender())
                .build());
    }
}
