package mops.repositories;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Module;
import mops.model.classes.Priority;
import mops.model.classes.Role;
import mops.services.ApplicantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
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
        Module module = Module.builder()
                .deadline(Instant.ofEpochSecond(100l))
                .name("Info4")
                .build();

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
                .minHours(7)
                .maxHours(17)
                .module(module)
                .priority(Priority.SehrHoch)
                .grade(1.3)
                .lecturer("Prof")
                .semester("SS2020")
                .role(Role.TUTOR)
                .build();
        Set<Application> applications = new HashSet<>();
        applications.add(application);


        applicant = Applicant.builder()
                .uniserial("ask111")
                .firstName("Hans")
                .surname("Anders")
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