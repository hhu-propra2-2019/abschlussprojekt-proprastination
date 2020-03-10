package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


class ApplicantTest {
    Applicant applicant;
    Certificate cert;
    Address address;
    List<Application> applicationList;

    @BeforeEach
    void init() {
        Application application = Application.builder().module("Divination").build();
        applicationList = Arrays.asList(application);
        cert = Certificate.builder()
                .name("Bachelor")
                .university("Harvard")
                .build();
        address = Address.builder()
                .street("Baker Street 21B")
                .city("London")
                .country("England")
                .zipcode(20394)
                .build();

        applicant = Applicant.builder()
                .name("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status(Status.NEW)
                .cert(cert)
                .applications(applicationList)
                .build();
    }

    @Test
    void testBuilder() {

        assertThat(applicant)
                .hasFieldOrPropertyWithValue("name", "J")
                .hasFieldOrPropertyWithValue("birthday", "01.01.2001")
                .hasFieldOrPropertyWithValue("birthplace", "Wakanda")
                .hasFieldOrPropertyWithValue("course", "Arts")
                .hasFieldOrPropertyWithValue("address", address)
                .hasFieldOrPropertyWithValue("nationality", "English")
                .hasFieldOrPropertyWithValue("status", Status.NEW)
                .hasFieldOrPropertyWithValue("cert", cert)
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
        assertThat(applicant.toString()).isEqualTo("Applicant(name=J, birthplace=Wakanda, address=Address(street=Baker Street 21B, city=London, country=England, zipcode=20394), birthday=01.01.2001, nationality=English, course=Arts, status=Status.NEW, cert=Certificate(name=Bachelor, university=Harvard), applications=[Application(hours=0, module=Divination, grade=0.0, lecturer=null, semester=null, comment=null, role=null)])");
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