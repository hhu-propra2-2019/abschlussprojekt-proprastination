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
                .university("Batcave")
                .name("Basic Training")
                .build();

        application = Application.builder()
                .applicantusername("bob111")
                .priority(2)
                .module("Hausbau")
                .hours(17)
                .comment("Boooob der Baumeister")
                .grade(1.3)
                .lecturer("Lala der Teletubby")
                .role(Role.KORREKTOR)
                .semester("SS2020")
                .build();

        application2 = Application.builder()
                .applicantusername("bob111")
                .priority(1)
                .module("Rächer")
                .hours(99)
                .comment("Ich bin Batman")
                .grade(1.0)
                .lecturer("Ich selbst?")
                .role(Role.BOTH)
                .semester("Immer")
                .build();

        applicant = Applicant.builder()
                .application(application)
                .application(application2)
                .status(Status.NEW)
                .course("Hausbau")
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

    }


    @Test
    void createApplication() {


        Application application1 = service.createApplication("bob111", "Hausbau", "Lala der Teletubby", "SS2020", "Boooob der Baumeister", 17, 1.3, Role.KORREKTOR, 2);

        assertThat(application1).isEqualTo(application);
    }

    @Test
    void createApplicant() {
        Applicant applicant1 = service.createApplicant("Batman", "Gotham", address, "11.11.1111", "US of A**", "Hausbau", Status.NEW, cert, Arrays.asList(application, application2));

        assertThat(applicant1).isEqualTo(applicant);
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