package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DistributionTest {
    Distribution dist;
    List<Applicant> emps;

    @BeforeEach
    void init() {
        Module module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Application application = Application.builder().module(module).build();
        Set<Application> applicationList = new HashSet<>();
        applicationList.add(application);

        Certificate cert = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address address = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("20394")
                .build();

        Applicant applicant = Applicant.builder()
                .surname("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(cert)
                .applications(applicationList)
                .build();
        emps = Arrays.asList(applicant);
        dist = Distribution.builder()
                .employees(emps)
                .module(module)
                .build();
    }

    @Test
    void testBuilder() {
        Module module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        assertThat(dist)
                .hasFieldOrPropertyWithValue("module", module)
                .hasFieldOrPropertyWithValue("employees", emps);
    }

    @Test
    void testToString() {
        assertThat(dist.toString()).isEqualTo("Distribution(module=Module(name=Info4, deadlineDate=null, deadlineTime=null, " +
                "deadline=1970-01-01T00:01:40, shortName=null, profSerial=null, sevenHourLimit=null, nineHourLimit=null, " +
                "seventeenHourLimit=null), employees=[Applicant(uniserial=null, birthplace=Wakanda, firstName=null," +
                " surname=J, address=Address(street=Baker Street, houseNumber=21B, city=London, country=England," +
                " zipcode=20394), checked=false, collapsed=false, gender=null, birthday=01.01.2001, nationality=English, course=Arts, status=New," +
                " comment=null, certs=Certificate(name=Bachelor, course=Harvard), applications=["+
                "Application(minHours=0, finalHours=0, maxHours=0, module=Module(name=Info4," +
                " deadlineDate=null, deadlineTime=null, deadline=1970-01-01T00:01:40, shortName=null, profSerial=null," +
                " sevenHourLimit=null, nineHourLimit=null, seventeenHourLimit=null), priority=null, grade=0.0," +
                " lecturer=null, semester=null, role=null, comment=null)])])"
        );
    }

    @Test
    void testNoArgsConstructor() {
        Distribution emptyDistribution = new Distribution();
        assertNull(emptyDistribution.getModule());
        assertNull(emptyDistribution.getEmployees());
    }

    @Test
    void testEquals() {
        Module newModule = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Application newApplication = Application.builder().module(newModule).build();
        Set<Application> applicationList = new HashSet<>();
        applicationList.add(newApplication);

        Certificate newCert = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address newAddress = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("20394")
                .build();

        Applicant newApplicant = Applicant.builder()
                .surname("J")
                .address(newAddress)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(newCert)
                .applications(applicationList)
                .build();
        List<Applicant> newEmps = Arrays.asList(newApplicant);
        Distribution newDist = Distribution.builder()
                .employees(newEmps)
                .module(newModule)
                .build();

        assertEquals(dist, newDist);
        assertEquals(newDist, dist);
    }

    @Test
    void testEqualsNotEqual() {
        Module newModule = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info6")
                .build();
        Application newApplication = Application.builder().module(newModule).build();
        Set<Application> applicationList = new HashSet<>();
        applicationList.add(newApplication);

        Certificate newCert = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address newAddress = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("20394")
                .build();

        Applicant newApplicant = Applicant.builder()
                .surname("J")
                .address(newAddress)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(newCert)
                .applications(applicationList)
                .build();
        List<Applicant> newEmps = Arrays.asList(newApplicant);
        Distribution newDist = Distribution.builder()
                .employees(newEmps)
                .module(newModule)
                .build();

        assertNotEquals(dist, newDist);
        assertNotEquals(newDist, dist);
    }

    @Test
    void testHashCode() {
        Module newModule = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Application newApplication = Application.builder().module(newModule).build();
        Set<Application> applicationList = new HashSet<>();
        applicationList.add(newApplication);

        Certificate newCert = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address newAddress = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("20394")
                .build();

        Applicant newApplicant = Applicant.builder()
                .surname("J")
                .address(newAddress)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(newCert)
                .applications(applicationList)
                .build();
        List<Applicant> newEmps = Arrays.asList(newApplicant);
        Distribution newDist = Distribution.builder()
                .employees(newEmps)
                .module(newModule)
                .build();

        int hashCode = dist.hashCode();
        int newHashCode = newDist.hashCode();

        assertEquals(hashCode, newHashCode);
    }

    @Test
    void testHashCodeNotEqual() {
        Module newModule = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info6")
                .build();
        Application newApplication = Application.builder().module(newModule).build();
        Set<Application> applicationList = new HashSet<>();
        applicationList.add(newApplication);

        Certificate newCert = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        Address newAddress = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("20394")
                .build();

        Applicant newApplicant = Applicant.builder()
                .surname("J")
                .address(newAddress)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .certs(newCert)
                .applications(applicationList)
                .build();
        List<Applicant> newEmps = Arrays.asList(newApplicant);
        Distribution newDist = Distribution.builder()
                .employees(newEmps)
                .module(newModule)
                .build();

        int hashCode = dist.hashCode();
        int newHashCode = newDist.hashCode();

        assertNotEquals(hashCode, newHashCode);
    }
}
