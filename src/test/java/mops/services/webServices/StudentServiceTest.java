package mops.services.webServices;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentServiceTest {

    private ApplicantService applicantServiceMock = mock(ApplicantService.class);
    private ModuleService moduleServiceMock = mock(ModuleService.class);

    private  StudentService studentService = new StudentService(applicantServiceMock, moduleServiceMock);

    private WebAddress webAddress = WebAddress.builder()
            .city("M-Stadt")
            .country("Musterania")
            .number("42")
            .street("Musterstraße")
            .zipcode("77777")
            .build();

    private Address address = Address.builder()
            .street("Musterstraße")
            .city("M-Stadt")
            .country("Musterania")
            .houseNumber("42")
            .zipcode("77777")
            .build();

    private WebCertificate webCertificate = WebCertificate.builder()
            .graduation("Master")
            .graduationcourse("Desaster")
            .build();

    private Certificate certificate = Certificate.builder()
            .name("Master")
            .course("Desaster")
            .build();

    private WebApplicant webApplicant = WebApplicant.builder()
            .birthday("2001-01-01")
            .birthplace("London, UK")
            .comment("Ah!")
            .course("Photografie")
            .gender("male")
            .nationality("UK")
            .status("Tutor")
            .build();

    private Applicant applicant = Applicant.builder()
            .uniserial("jabon007")
            .firstName("James")
            .surname("Bond")
            .address(address)
            .certs(certificate)
            .birthday("2001-01-01")
            .birthplace("London, UK")
            .comment("Ah!")
            .course("Photografie")
            .gender("male")
            .nationality("UK")
            .status("Tutor")
            .checked(false)
            .build();

    private WebCertificate webCertificate1;
    private WebCertificate webCertificate2;

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
        webCertificate1 = WebCertificate.builder()
                .graduation("Bachelor")
                .graduationcourse("Informatik")
                .build();
        webCertificate2 = WebCertificate.builder()
                .graduationcourse("none")
                .graduation("")
                .build();
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
        webCertificate1 = WebCertificate.builder()
                .graduation("Bachelor")
                .graduationcourse("Informatik")
                .build();
        webCertificate2 = WebCertificate.builder()
                .graduationcourse("none")
                .graduation("")
                .build();
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
    void buildApplicantTest2() {
        webCertificate1 = WebCertificate.builder()
                .graduation("Bachelor")
                .graduationcourse("Informatik")
                .build();
        webCertificate2 = WebCertificate.builder()
                .graduationcourse("none")
                .graduation("")
                .build();
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
        // Saving applicant to database.
        applicantServiceMock.saveApplicant(applicant);
        Applicant applicantTest = studentService.buildApplicant("mamus100", webApplicant, address, certificate,
                "Max", "Mustermann");
        assertThat(applicant).isEqualTo(applicantTest);
    }

    @Test
    void buildAddress() {
        Address result = studentService.buildAddress(webAddress);

        assertEquals(result, address);
    }

    @Test
    void buildCertificate() {
        Certificate result = studentService.buildCertificate(webCertificate);

        assertEquals(result, certificate);
    }

    @Test
    void buildApplicantIfApplicantNotFound() {
        String uniserial = "jabon007";
        String givenName = "James";
        String familyName = "Bond";
        when(applicantServiceMock.findByUniserial(uniserial)).thenReturn(null);

        Applicant result = studentService.buildApplicant(uniserial, webApplicant,
                address, certificate, givenName, familyName);

        verify(applicantServiceMock, times(1)).findByUniserial(uniserial);
        assertEquals(applicant, result);
    }

    @Test
    void buildApplicantIfApplicantFound() {
        String uniserial = "jabon007";
        String givenName = "James";
        String familyName = "Bond";
        when(applicantServiceMock.findByUniserial(uniserial)).thenReturn(applicant);

        Applicant result = studentService.buildApplicant(uniserial, webApplicant,
                address, certificate, givenName, familyName);

        verify(applicantServiceMock, times(1)).findByUniserial(uniserial);
        assertEquals(applicant, result);
    }

    @Test
    void updateApplicantWithoutChangingApplications() {
        Set<Application> testApplications = new HashSet<>();
        testApplications.add(mock(Application.class));
        testApplications.add(mock(Application.class));

        Applicant oldApplicant = Applicant.builder()
                .uniserial("jabon007")
                .firstName("James")
                .surname("Bond")
                .address(mock(Address.class))
                .certs(mock(Certificate.class))
                .birthday("2001-01-01")
                .birthplace("London, UK")
                .comment("Ah!")
                .course("Photografie")
                .gender("male")
                .nationality("UK")
                .status("Tutor")
                .checked(false)
                .applications(testApplications)
                .build();

        Applicant newApplicant = Applicant.builder()
                .uniserial("jabon007")
                .firstName("Clark")
                .surname("Kent")
                .address(mock(Address.class))
                .certs(mock(Certificate.class))
                .birthday("2002-02-02")
                .birthplace("Krypton")
                .comment("Up, up and away!")
                .course("Journalism")
                .gender("female")
                .nationality("USA")
                .status("Korrektor")
                .checked(false)
                .applications(testApplications)
                .build();

        when(applicantServiceMock.findByUniserial("jabon007")).thenReturn(oldApplicant);

        studentService.updateApplicantWithoutChangingApplications(newApplicant);

        verify(applicantServiceMock, times(1)).findByUniserial("jabon007");
        verify(applicantServiceMock, times(1)).saveApplicant(newApplicant);
    }

    @Test
    void getExistingApplicant() {
        WebApplicant result = studentService.getExistingApplicant(applicant);

        assertEquals(webApplicant, result);
    }

    @Test
    void getExistingAddress() {
        WebAddress result = studentService.getExistingAddress(address);

        assertEquals(webAddress, result);
    }

    @Test
    void getExistingCertificate() {
        WebCertificate result = studentService.getExistingCertificate(certificate);

        assertEquals(webCertificate, result);
    }

    @Test
    void getAllNotfilledModulesApplicantIsNull() {
        Module moduleMock1 = mock(Module.class);
        Module moduleMock2 = mock(Module.class);
        List<Module> modules = new ArrayList<>();
        modules.add(moduleMock1);
        modules.add(moduleMock2);

        List<Module> result = studentService.getAllNotfilledModules(null, modules);

        assertEquals(modules, result);
    }

    @Test
    void getAllNotfilledModulesApplicationsGetSortedOut() {
        Module applied = mock(Module.class);
        when(applied.getDeadline()).thenReturn(LocalDateTime.MAX);
        Module notApplied = mock(Module.class);
        when(notApplied.getDeadline()).thenReturn(LocalDateTime.MAX);
        List<Module> modules = new ArrayList<>();
        modules.add(applied);
        modules.add(notApplied);
        Applicant applicantMock = mock(Applicant.class);
        Application applicationMock = mock(Application.class);
        Set<Application> applications = new HashSet<>();
        applications.add(applicationMock);
        when(applicantMock.getApplications()).thenReturn(applications);
        when(applicationMock.getModule()).thenReturn(applied);

        List<Module> result = studentService.getAllNotfilledModules(applicantMock, modules);

        assertThat(result).containsOnly(notApplied);
    }

    @Test
    void getAllNotfilledModulesExpiredDeadlinesGetSortedOut() {
        Module notExpired = mock(Module.class);
        when(notExpired.getDeadline()).thenReturn(LocalDateTime.MAX);
        Module expired = mock(Module.class);
        when(expired.getDeadline()).thenReturn(LocalDateTime.MIN);
        List<Module> modules = new ArrayList<>();
        modules.add(notExpired);
        modules.add(expired);
        Applicant applicantMock = mock(Applicant.class);
        when(applicantMock.getApplications()).thenReturn(new HashSet<>());

        List<Module> result = studentService.getAllNotfilledModules(applicantMock, modules);

        assertThat(result).containsOnly(notExpired);
    }

}
