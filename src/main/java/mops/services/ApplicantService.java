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

    public void updateApplicantWithouChangingApplications(final Applicant newApplicant) {
        Applicant oldApplicant = findByUsername(newApplicant.getUniserial());
        Applicant generatedApplicant = Applicant.builder()
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
                .title(newApplicant.getTitle())
                .surname(newApplicant.getSurname())
                .firstName(newApplicant.getFirstName())
                .gender(newApplicant.getGender())
                .build();
    }
}
