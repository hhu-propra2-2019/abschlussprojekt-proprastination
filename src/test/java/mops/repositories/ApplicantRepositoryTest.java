package mops.repositories;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.services.ApplicantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class ApplicantRepositoryTest {


    @Autowired
    ApplicantService service;

    Applicant applicant;
    Application application;
    Certificate cert;
    Address address;


    @Test
    public void test() {

        address = Address.builder()
                .street("Allee")
                .street("1")
                .city("Baumberg")
                .country("Bergland")
                .zipcode(22222)
                .build();
        cert = Certificate.builder()
                .name("Uni")
                .course("Hauskunde")
                .build();
        application = Application.builder()
                .hours(17)
                .module("Info")
                .priority(1)
                .grade(1.3)
                .lecturer("Prof")
                .semester("SS2020")
                .role("Tutor")
                .build();
        Set<Application> applications = new HashSet<>();
        applications.add(application);


        applicant = Applicant.builder()
                .uniserial("ask111")
                .firstName("Hans")
                .surname("Anders")
                .title("Baumgarten")
                .address(address)
                .birthday("20.20.2020")
                .course("Hausbau")
                .nationality("Bergman")
                .birthplace("Baumgarten")
                .status("Continue")
                .comment("JOO NICE")
                .certs(cert)
                .applications(applications).build();

        service.saveApplicant(applicant);
    }

}