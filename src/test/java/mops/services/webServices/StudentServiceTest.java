package mops.services.webServices;

import com.github.javafaker.App;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Certificate;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

}