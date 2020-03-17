package mops.services;

import mops.db.repositories.ApplicantRepository;
import mops.model.classes.Applicant;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

@Service
@EnableAutoConfiguration
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    public ApplicantService(final ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public Applicant findByUsername(final String username) {
        return applicantRepository.findByName(username);
    }
}
