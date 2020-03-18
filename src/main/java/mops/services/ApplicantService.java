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

    public ApplicantService(final ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public Applicant findByUsername(final String username) {
        return applicantRepository.findByFirstName(username);
    }

    public void saveApplicant(final Applicant applicant) {
        applicantRepository.save(applicant);
    }

    public List<Applicant> findAll() {
        return applicantRepository.findAll();
    }

    public Applicant findByUniserial(final String uniserial) {
        return applicantRepository.findByUniserial(uniserial).get(0);
    }
}
