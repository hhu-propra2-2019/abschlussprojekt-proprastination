package mops.services;

import mops.model.classes.*;
import mops.model.classes.Module;
import mops.repositories.ModuleRepository;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ApplicationServiceTest {

    @Autowired
    ApplicantService applicantService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    ModuleRepository moduleRepository;

    List<Application> applications1;
    Address address;
    Certificate cert;
    Applicant applicant;
    Application application1;
    Application application2;
    Module module;

    @AfterAll
    void setDown() {
        applicantService.deleteAll();
        moduleRepository.deleteAll();
        applicantService.deleteAll();
    }

    @BeforeAll
    void setUp() {
        address = Address.builder()
                .city("Danville")
                .country("USA")
                .zipcode("10101a")
                .street("Straße")
                .houseNumber("1a")
                .build();

        cert = Certificate.builder()
                .course("Experimentation")
                .name("Bachelor")
                .build();

        module = Module.builder()
                .deadline(Instant.ofEpochSecond(100l))
                .name("howToBeEvil")
                .profSerial("prodo111")
                .build();
        application1 = Application.builder()
                .priority(Priority.HIGH)
                .module(module)
                .minHours(7)
                .maxHours(17)
                .grade(1.0)
                .lecturer("Dr. Heinz Doofenshmirtz")
                .role(Role.BOTH)
                .semester("SS2020")
                .build();

        application2 = Application.builder()
                .priority(Priority.HIGH)
                .module(module)
                .minHours(9)
                .maxHours(9)
                .grade(4.0)
                .lecturer("Perry das Schnabeltier")
                .role(Role.PROOFREADER)
                .semester("Immer")
                .build();

        applications1 = new ArrayList<>();
        applications1.add(application1);
        applications1.add(application2);

        applicant = Applicant.builder()
                .firstName("Phineas")
                .surname("Flynn")
                .uniserial("phifl100")
                .status("Änderung")
                .certs(cert)
                .applications(applications1)
                .course("be evil")
                .gender("männlich")
                .nationality("USA")
                .comment("Where's Perry")
                .birthplace("Danville")
                .address(address)
                .birthday("2002-10-03")
                .build();
    }

    @Test
    void contextLoad() {}

    @Test
    void saveTest() {
        moduleRepository.save(module);
        applicantService.saveApplicant(applicant);
        applicationService.save(application1);
    }

    @Test
    void findAllByModuleTestID() {
    }

    @Test
    void findAll() {}

    @Test
    void deleteAll() {}
}
