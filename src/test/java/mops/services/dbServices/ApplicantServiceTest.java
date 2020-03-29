package mops.services.dbServices;

import mops.model.classes.*;
import mops.model.classes.Module;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ApplicantServiceTest {

    @Autowired
    ApplicantService applicantService;

    @Autowired
    ModuleRepository moduleRepository;

    Applicant applicant;
    Application application1;
    Application application2;
    Certificate cert;
    Address address;
    Module module;

    @AfterEach
    void setdown() {
        applicantService.deleteAll();
        moduleRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        address = Address.builder()
                .city("Gotham")
                .country("USA")
                .zipcode("42069")
                .street("Batstreet 1")
                .build();

        cert = Certificate.builder()
                .course("Batcave")
                .name("Basic Training")
                .build();

        application1 = Application.builder()
                .priority(Priority.HIGH)
                .module(module)
                .minHours(7)
                .maxHours(17)
                .grade(1.3)
                .lecturer("Lala der Teletubby")
                .role(Role.PROOFREADER)
                .semester("SS2020")
                .build();

        application2 = Application.builder()
                .priority(Priority.HIGH)
                .module(module)
                .minHours(5)
                .maxHours(99)
                .grade(1.0)
                .lecturer("Ich selbst?")
                .role(Role.PROOFREADER)
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
        moduleRepository.save(module);
        applicantService.saveApplicant(applicant);

        Applicant test = applicantService.findByUniserial(applicant.getUniserial());

        assertThat(test).isEqualTo(applicant);
    }

    @Test
    void createApplication() {
        Module module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();

        Application.builder()
                .grade(1.3)
                .minHours(9)
                .maxHours(17)
                .semester("SS2020")
                .lecturer("Lala der Teletubby")
                .module(module)
                .role(Role.PROOFREADER)
                .build();

        assertThat(application1).isEqualTo(application1);
    }

    @Test
    void testDeleteApplication() {
        moduleRepository.save(module);
        applicantService.saveApplicant(applicant);

        applicantService.deleteApplication(application1, applicant);

        Applicant resultApplicant = applicantService.findByUniserial(applicant.getUniserial());
        assertThat(resultApplicant.getApplications()).doesNotContain(application1);
        assertThat(resultApplicant.getApplications()).contains(application2);
    }

    @Test
    void deleteSecondApplication() {
        moduleRepository.save(module);
        applicantService.saveApplicant(applicant);

        applicantService.deleteApplication(application2, applicant);

        Applicant resultApplicant = applicantService.findByUniserial(applicant.getUniserial());
        assertThat(resultApplicant.getApplications()).contains(application1);
        assertThat(resultApplicant.getApplications()).doesNotContain(application2);
    }

    @Test
    void deleteNotFoundApplication() {
        moduleRepository.save(module);
        applicantService.saveApplicant(applicant);
        Application application3 = Application.builder()
                .priority(Priority.NEUTRAL)
                .module(module)
                .minHours(9)
                .maxHours(9)
                .grade(3.0)
                .lecturer("Charles Xavier")
                .role(Role.BOTH)
                .semester("SS2019")
                .build();

        applicantService.deleteApplication(application3, applicant);

        Applicant resultApplicant = applicantService.findByUniserial(applicant.getUniserial());
        assertThat(resultApplicant.getApplications()).contains(application1);
        assertThat(resultApplicant.getApplications()).contains(application2);
        assertThat(resultApplicant.getApplications()).doesNotContain(application3);
    }

    @Test
    void testDeleteApplicationSetOfApplicationsIsEmpty() {
        Set<Application> applications = new HashSet<>();
        Applicant testApplicant = Applicant.builder()
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

        applicantService.deleteApplication(application1, testApplicant);

        Applicant resultApplicant = applicantService.findByUniserial(testApplicant.getUniserial());
        assertThat(resultApplicant.getApplications()).isEmpty();
    }

    @Test
    void testFindByApplications() {
        moduleRepository.save(module);
        applicantService.saveApplicant(applicant);

        Applicant result = applicantService.findByApplications(application1);

        assertEquals(applicant.getId(), result.getId());
    }

}
