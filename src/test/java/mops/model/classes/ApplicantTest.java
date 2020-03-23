package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


class ApplicantTest {
    Applicant applicant;
    Certificate certs;
    Address address;
    Set<Application> applicationList;

    @BeforeEach
    void init() {
        Application application = Application.builder().module("Divination").build();
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
                .zipcode(20394)
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
        assertThat(applicant.toString()).isEqualTo("Applicant(uniserial=null, " +
                "birthplace=Wakanda, firstName=null, surname=J, address=Address(street=Baker Street, " +
                "houseNumber=21B, city=London, country=England, zipcode=20394), gender=null, " +
                "birthday=01.01.2001, nationality=English, course=Arts, status=New, " +
                "comment=null, certs=Certificate(name=Bachelor, course=Harvard), " +
                "applications=[Application(minHours=0, finalHours=0, maxHours=0, " +
                "module=Divination, priority=0, grade=0.0, lecturer=null, semester=null, " +
                "role=null, comment=null)])");
    }
}