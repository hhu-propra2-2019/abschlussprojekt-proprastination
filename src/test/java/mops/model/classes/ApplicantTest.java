package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


class ApplicantTest {
    Applicant applicant;
    Certificate certs;
    Address address;
    List<Application> applicationList;

    @BeforeEach
    void init() {
        Application application = Application.builder().module("Divination").build();
        applicationList = Arrays.asList(application);
        certs = Certificate.builder()
                .name("Bachelor")
                .course("Harvard")
                .build();
        address = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode(20394)
                .build();

        applicant = Applicant.builder()
                .surname("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status(Status.NEW)
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
                .hasFieldOrPropertyWithValue("status", Status.NEW)
                .hasFieldOrPropertyWithValue("certs", certs)
                .hasFieldOrPropertyWithValue("applications", applicationList);

    }

    @Test
    void testEquals() {
        Applicant.ApplicantBuilder applicantBuilder = applicant.toBuilder();
        Applicant applicant1 = applicantBuilder.build();

        assertThat(applicant).isEqualTo(applicant1);

    }

    @Test
    void testToString() {
        assertThat(applicant.toString()).isEqualTo("Applicant(title=null, firstName=null, surname=J, address=Address(street=Baker Street, houseNumber=21B, city=London, country=England, zipcode=20394), gender=null, birthday=01.01.2001, nationality=English, birthplace=Wakanda, course=Arts, status=Status.NEW, certs=Certificate(name=Bachelor, university=Harvard), applications=[Application(applicantusername=hans222, hours=0, module=Divination, priority=0, grade=0.0, lecturer=null, semester=null, comment=null, role=null)])");
    }

    @Test
    void testToBuilder() {
        Applicant.ApplicantBuilder applicantBuilder = applicant.toBuilder();
        Applicant applicant1 = applicantBuilder.build();

        Applicant.ApplicantBuilder applicantBuilder1 = applicant1.toBuilder();
        Applicant applicant2 = applicantBuilder1.build();

        assertThat(applicant1).isEqualTo(applicant2);
    }
}