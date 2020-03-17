package mops.db.repositories;

import mops.db.dto.AddressDTO;
import mops.db.dto.ApplicantDTO;
import mops.db.dto.ApplicationDTO;
import mops.db.dto.CertificateDTO;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Role;
import mops.model.classes.Status;
import mops.services.ApplicantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicantRepositoryTest {

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    ApplicationRepository apprepo;

    @Autowired
    ApplicantService service;

    ApplicantDTO applicant;
    ApplicationDTO application;
    ApplicationDTO application2;
    CertificateDTO cert;
    AddressDTO address;


    @Test
    public void test() {

        address = new AddressDTO("Allee 1", "Baumberg", "Bergland", 22222);
        cert = new CertificateDTO("Hauskunde", "UNI");
        application = new ApplicationDTO(null, 17, "Info", 1, 1.3, "Prof", "SS2020", Role.TUTOR);
        Set<ApplicationDTO> applications = new HashSet<>();
        applications.add(application);
        applicant = new ApplicantDTO(null, "ask111", "Hans", "Anders", "Baumgarten", address, "20.20.2020", "Bergman", "Haubau", Status.CONTINUE, "JOO NICE", cert, applications);

        applicantRepository.save(applicant);

        ApplicantDTO app3 = applicantRepository.findAll().iterator().next();

        var check = apprepo.findAll();

        var check2 = service.getAllApplications();
    }

}