package mops.services.webServices;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Certificate;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class StudentServiceTest {

    @Autowired
    ApplicantService applicantService;

    @Autowired
    StudentService studentService;

    @Autowired
    ModuleService moduleService;

    WebAddress webAddress;
    WebApplicant webApplicant;
    WebCertificate webCertificate1;
    WebCertificate webCertificate2;

    @BeforeAll
    void setup() {
        webAddress = WebAddress.builder()
                .city("Düsseldorf")
                .country("Deutschland")
                .street("Universitätsstraße")
                .number("1")
                .zipcode("40225")
                .build();
        webCertificate1 = WebCertificate.builder()
                .graduation("Bachelor")
                .graduationcourse("Informatik")
                .build();
        webCertificate2 = WebCertificate.builder()
                .graduationcourse("none")
                .graduation("")
                .build();
        webApplicant = WebApplicant.builder()
                .birthday("2000-02-02")
                .birthplace("Düsseldorf")
                .gender("männlich")
                .nationality("Deutschland")
                .course("Informatik")
                .status("Einstellung")
                .comment("Ich mag Züge")
                .build();
    }

    @AfterEach
    void doAfterEach() {
        applicantService.deleteAll();
    }

    @Test
    void buildAddressTest() {
        Address address = Address.builder()
                .street(webAddress.getStreet())
                .houseNumber(webAddress.getNumber())
                .city(webAddress.getCity())
                .zipcode(webAddress.getZipcode())
                .country(webAddress.getCountry())
                .build();
        Address address1 = studentService.buildAddress(webAddress);
        assertThat(address).isEqualTo(address1);
    }

    @Test
    void buildCertificateTest() {
        Certificate certificate1 =  Certificate.builder()
                .name(webCertificate1.getGraduation())
                .course(webCertificate1.getGraduationcourse())
                .build();
        Certificate certificate2 =  Certificate.builder()
                .name(webCertificate2.getGraduation())
                .course(webCertificate2.getGraduationcourse())
                .build();
        Certificate certificateTest1 = studentService.buildCertificate(webCertificate1);
        Certificate certificateTest2 = studentService.buildCertificate(webCertificate2);
        assertThat(certificate1).isEqualTo(certificateTest1);
        assertThat(certificate2).isEqualTo(certificateTest2);
    }

    @Test
    void buildApplicantTest1() {
        Address address = studentService.buildAddress(webAddress);
        Certificate certificate = studentService.buildCertificate(webCertificate1);
        Applicant applicant = Applicant.builder()
                .uniserial("mamus100")
                .firstName("Max")
                .surname("Mustermann")
                .address(address)
                .birthday(webApplicant.getBirthday())
                .birthplace(webApplicant.getBirthplace())
                .gender(webApplicant.getGender())
                .nationality(webApplicant.getNationality())
                .course(webApplicant.getCourse())
                .status(webApplicant.getStatus())
                .certs(certificate)
                .comment(webApplicant.getComment())
                .checked(false)
                .build();
        Applicant applicantTest = studentService.buildApplicant("mamus100", webApplicant, address, certificate,
                "Max", "Mustermann");
        assertThat(applicant).isEqualTo(applicantTest);

    }

    @Test
    void buildApplicantTets2() {
        Address address = studentService.buildAddress(webAddress);
        Certificate certificate = studentService.buildCertificate(webCertificate1);
        Applicant applicant = Applicant.builder()
                .uniserial("mamus100")
                .firstName("Max")
                .surname("Mustermann")
                .address(address)
                .birthday(webApplicant.getBirthday())
                .birthplace(webApplicant.getBirthplace())
                .gender(webApplicant.getGender())
                .nationality(webApplicant.getNationality())
                .course(webApplicant.getCourse())
                .status(webApplicant.getStatus())
                .certs(certificate)
                .comment(webApplicant.getComment())
                .checked(false)
                .build();
        applicantService.saveApplicant(applicant);
        Applicant applicantTest = studentService.buildApplicant("mamus100", webApplicant, address, certificate,
                "Max", "Mustermann");
        assertThat(applicant).isEqualTo(applicantTest);
    }

/*    @Test
    void updateApplicantTest() {
        Address address = studentService.buildAddress(webAddress);
        Certificate certificate1 = studentService.buildCertificate(webCertificate1);
        Certificate certificate2 = studentService.buildCertificate(webCertificate2);
        Applicant applicant1 = studentService.buildApplicant("mamus100", webApplicant, address, certificate1,
                "Max", "Mustermann");
        applicantService.saveApplicant(applicant1);
        Applicant applicant2 = studentService.buildApplicant("mamus100", webApplicant, address, certificate2,
        "Max", "Mustermann");
        studentService.updateApplicantWithoutChangingApplications(applicant2);
        Applicant applicantTest = applicantService.findByUniserial("mamus100");
        assertThat(applicant2).isEqualTo(applicantTest);
    }*/

}
