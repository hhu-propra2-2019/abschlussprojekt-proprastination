package mops.services;

import mops.model.classes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ApplicantServiceTest {

    @Autowired
    ApplicantService service;


    Applicant applicant;
    Application application;
    Application application2;
    Certificate cert;
    Address address;

    @BeforeAll
    void setup() {
        address = Address.builder()
                .city("Gotham")
                .country("USA")
                .zipcode(42069)
                .street("Batstreet 1")
                .build();

        cert = Certificate.builder()
                .course("Batcave")
                .name("Basic Training")
                .build();

        application = Application.builder()
                .priority(2)
                .module("Hausbau")
                .hours(17)
                .grade(1.3)
                .lecturer("Lala der Teletubby")
                .role(Role.KORREKTOR)
                .semester("SS2020")
                .build();

        application2 = Application.builder()
                .priority(1)
                .module("Rächer")
                .hours(99)
                .grade(1.0)
                .lecturer("Ich selbst?")
                .role(Role.BOTH)
                .semester("Immer")
                .build();

        applicant = Applicant.builder()
                .application(application)
                .application(application2)
                .status(Status.NEW)
                .surename("dumm")
                .uniserial("sss111")
                .course("Hausbau")
                .comment("WOW!")
                .birthday("11.11.1111")
                .address(address)
                .nationality("US of A**")
                .birthplace("Gotham")
                .name("Batman")
                .certs(cert)
                .build();
    }

    @Test
    void saveApplicant() {
        service.save(applicant);

        var test = service.getApplicant(applicant.getUniserial());

    }

    @Test
    void createApplicant() {
    }

    @Test
    void save() {
    }

    @Test
    void testSave() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void getAllApplications() {
    }

    @Test
    void getAllApplicationsByModule() {
    }

    @Test
    void getAllIterable() {
    }

    @Test
    void getAll() {
    }
}