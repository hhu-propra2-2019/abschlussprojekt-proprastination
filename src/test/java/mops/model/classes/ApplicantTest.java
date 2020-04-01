package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ApplicantTest {
    Applicant applicant;
    Certificate certs;
    Address address;
    Set<Application> applicationList;

    @BeforeEach
    void init() {
        Module module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Application application = Application.builder().module(module).build();
        applicationList = new HashSet<>();
        applicationList.add(application);
        certs = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        address = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("NW1 6XE")
                .build();

        applicant = Applicant.builder()
                .surname("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(certs)
                .applications(applicationList)
                .build();
    }

    @Test
    void testBuilder() {

        assertThat(applicant)
                .hasFieldOrPropertyWithValue("surname", "J")
                .hasFieldOrPropertyWithValue("birthday", "01.01.2001")
                .hasFieldOrPropertyWithValue("birthplace", "Wakanda")
                .hasFieldOrPropertyWithValue("course", "Arts")
                .hasFieldOrPropertyWithValue("address", address)
                .hasFieldOrPropertyWithValue("nationality", "English")
                .hasFieldOrPropertyWithValue("status", "New")
                .hasFieldOrPropertyWithValue("certs", certs)
                .hasFieldOrPropertyWithValue("applications", applicationList);

    }


    @Test
    void testToString() {
        assertThat(applicant.toString()).isEqualTo("Applicant(uniserial=null, birthplace=Wakanda, firstName=null," +
                " surname=J, address=Address(street=Baker Street, houseNumber=21B, city=London, country=England," +
                " zipcode=NW1 6XE), checked=false, collapsed=false, gender=null, birthday=01.01.2001, nationality=English, course=Arts, status=New," +
                " comment=null, certs=Certificate(name=Bachelor, course=Harvard), applications=[Application" +
                "(minHours=0, finalHours=0, maxHours=0, module=Module(name=Info4, deadlineDate=null, deadlineTime=null," +
                " deadline=1970-01-01T00:01:40, shortName=null, profSerial=null, sevenHourLimit=null, nineHourLimit=null," +
                " seventeenHourLimit=null), priority=null, grade=0.0, lecturer=null, semester=null, role=null, comment=null" +
                ")])");
    }

    @Test
    void testGetApplicationById() {
        Application applicationMock = mock(Application.class);
        when(applicationMock.getId()).thenReturn((long) 1);
        Set<Application> newApplicationList = new HashSet<>();
        newApplicationList.add(applicationMock);
        Applicant testApplicant = Applicant.builder()
                .surname("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(certs)
                .applications(newApplicationList)
                .build();

        Application result = testApplicant.getApplicationById(1);

        assertEquals(applicationMock, result);
    }

    @Test
    void testGetApplicationByIdMultipleApplications() {
        Application applicationMock1 = mock(Application.class);
        when(applicationMock1.getId()).thenReturn((long) 1);
        Application applicationMock2 = mock(Application.class);
        when(applicationMock2.getId()).thenReturn((long) 2);
        Application applicationMock3 = mock(Application.class);
        when(applicationMock3.getId()).thenReturn((long) 3);

        Set<Application> newApplicationList = new HashSet<>();
        newApplicationList.add(applicationMock1);
        newApplicationList.add(applicationMock2);
        newApplicationList.add(applicationMock3);
        Applicant testApplicant = Applicant.builder()
                .surname("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(certs)
                .applications(newApplicationList)
                .build();

        Application result1 = testApplicant.getApplicationById(1);
        Application result2 = testApplicant.getApplicationById(2);
        Application result3 = testApplicant.getApplicationById(3);

        assertEquals(applicationMock1, result1);
        assertEquals(applicationMock2, result2);
        assertEquals(applicationMock3, result3);
    }

    @Test
    void testGetApplicationByIdApplicationNotFound() {
        Application applicationMock = mock(Application.class);
        when(applicationMock.getId()).thenReturn((long) 1);
        Set<Application> newApplicationList = new HashSet<>();
        newApplicationList.add(applicationMock);
        Applicant testApplicant = Applicant.builder()
                .surname("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(certs)
                .applications(newApplicationList)
                .build();

        Application result = testApplicant.getApplicationById(2);

        assertNull(result);
    }

    @Test
    void testGetApplicationByIdApplicantHasNoApplications() {
        Application result = applicant.getApplicationById(1);

        assertNull(result);
    }

    @Test
    void testHashCode() {
        Module newModule = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Application newApplication = Application.builder().module(newModule).build();
        Set<Application> newApplicationList = new HashSet<>();
        newApplicationList.add(newApplication);
        Certificate newCerts = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address newAddress = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("NW1 6XE")
                .build();

        Applicant newApplicant = Applicant.builder()
                .surname("J")
                .address(newAddress)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(newCerts)
                .applications(newApplicationList)
                .build();

        int hashCode = applicant.hashCode();
        int newHashCode = newApplicant.hashCode();

        assertEquals(newHashCode, hashCode);
    }

    @Test
    void testHashCodeDifferentSurname() {
        Module newModule = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Application newApplication = Application.builder().module(newModule).build();
        Set<Application> newApplicationList = new HashSet<>();
        newApplicationList.add(newApplication);
        Certificate newCerts = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address newAddress = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("NW1 6XE")
                .build();

        Applicant newApplicant = Applicant.builder()
                .surname("Kerry")
                .address(newAddress)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(newCerts)
                .applications(newApplicationList)
                .build();

        int hashCode = applicant.hashCode();
        int newHashCode = newApplicant.hashCode();

        assertNotEquals(newHashCode, hashCode);
    }

    @Test
    void testNoArgsConstuctor() {
        Applicant emptyApplicant = new Applicant();

        assertNull(emptyApplicant.getAddress());
        assertNull(emptyApplicant.getApplications());
        assertNull(emptyApplicant.getBirthday());
        assertNull(emptyApplicant.getBirthplace());
        assertNull(emptyApplicant.getCerts());
        assertNull(emptyApplicant.getComment());
        assertNull(emptyApplicant.getCourse());
        assertNull(emptyApplicant.getFirstName());
        assertNull(emptyApplicant.getGender());
        assertNull(emptyApplicant.getNationality());
        assertNull(emptyApplicant.getStatus());
        assertNull(emptyApplicant.getSurname());
        assertNull(emptyApplicant.getUniserial());
    }
}
