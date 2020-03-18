package mops.services;

import mops.model.classes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ApplicantServiceTest {

    @Autowired
    ApplicantService applicantService;

    Applicant applicant;
    Application application1;
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

        application1 = Application.builder()
                .priority(0)
                .module("Hausbau")
                .hours(17)
                .grade(1.3)
                .lecturer("Lala der Teletubby")
                .role("Korrektor")
                .semester("SS2020")
                .build();

        application2 = Application.builder()
                .priority(1)
                .module("Rächer")
                .hours(99)
                .grade(1.0)
                .lecturer("Ich selbst?")
                .role("Both")
                .semester("Immer")
                .build();

        Set<Application> applications = new HashSet<>();
        applications.add(application1);
        applications.add(application2);

        applicant = Applicant.builder()
                .applications(applications)
                .status("New")
                .surname("dumm")
                .uniserial("sss111")
                .course("Hausbau")
                .comment("WOW!")
                .birthday("11.11.1111")
                .address(address)
                .nationality("US of A**")
                .birthplace("Gotham")
                .surname("Batman")
                .certs(cert)
                .build();
    }

    @Test
    void saveApplicant() {
        applicantService.saveApplicant(applicant);

        var test = applicantService.findByUniserial(applicant.getUniserial());
    }

    @Test
    void createApplication() {
        Application.builder()
                .grade(1.3)
                .hours(17)
                .semester("SS2020")
                .lecturer("Lala der Teletubby")
                .module("Hausbau")
                .role("Korrektor")
                .build();

        assertThat(application1).isEqualTo(application1);
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