package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ApplicationTest {
    Application application;
    Address address;
    Applicant applicant;
    Certificate certificate;

    @BeforeEach
    void setup() {
        address = Address.builder()
                .zipcode(12345)
                .country("USA")
                .city("Düsseldorf")
                .street("Street")
                .houseNumber("999")
                .build();

        certificate = Certificate.builder()
                .name("Bachelor")
                .course("Informatik")
                .build();

        applicant = Applicant.builder()
                .uniserial("lolol420")
                .address(address)
                .gender("male")
                .firstName("Angelo")
                .surname("Merkel")
                .comment("Moin")
                .nationality("Russian")
                .birthplace("Deutschland")
                .birthday("32.32.9999")
                .course("Trivial")
                .status("irelevant")
                .application(application)
                .certs(certificate)
                .build();

        application = Application.builder()
                .hours(2)
                .grade(1.3)
                .priority(1)
                .lecturer("Tester")
                .role("Korrektor")
                .semester("WS2020")
                .module("ProPra")
                .comment("")
                .applicant(applicant)
                .build();
    }

    @Test
    void TestBuilder() {
        //Arrange in BeforeEach

        assertThat(application)
                .hasFieldOrPropertyWithValue("hours", 2)
                .hasFieldOrPropertyWithValue("priority", 1)
                .hasFieldOrPropertyWithValue("grade", 1.3)
                .hasFieldOrPropertyWithValue("lecturer", "Tester")
                .hasFieldOrPropertyWithValue("role", "Korrektor")
                .hasFieldOrPropertyWithValue("semester", "WS2020")
                .hasFieldOrPropertyWithValue("module", "ProPra")
                .hasFieldOrPropertyWithValue("comment", "");


    }

    @Test
    void testEquals() {
        Application application1 = Application.builder()
                .hours(2)
                .grade(1.3)
                .priority(1)
                .lecturer("Tester")
                .role("Korrektor")
                .semester("WS2020")
                .module("ProPra")
                .build();

        Application application2 = Application.builder()
                .hours(2)
                .grade(1.3)
                .priority(1)
                .lecturer("Tester")
                .role("Korrektor")
                .semester("WS2020")
                .module("ProPra")
                .build();

        assertThat(application1).isEqualTo(application2);
    }

    @Test
    void testBuilderFailsWithMissingArgument() {
        assertThatThrownBy(() -> {
                    Application application = Application.builder()
                            .hours(2)
                            .build();
                }
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void testToString() {
        //Arrange in BeforeEach

        assertThat(application.toString()).isEqualTo(
                "Application(hours=2, module=ProPra, priority=1, grade=1.3, lecturer=Tester," +
                        " semester=WS2020, role=Korrektor, comment=, applicant=Applicant(uniserial=lolol420," +
                        " birthplace=Deutschland, firstName=Angelo, surname=Merkel," +
                        " address=Address(street=Street, houseNumber=999, city=Düsseldorf, country=USA," +
                        " zipcode=12345), gender=male, birthday=32.32.9999, nationality=Russian," +
                        " course=Trivial, status=irelevant, comment=Moin, certs=Certificate(name=Bachelor," +
                        " course=Informatik), applications=[null]))");

    }

}