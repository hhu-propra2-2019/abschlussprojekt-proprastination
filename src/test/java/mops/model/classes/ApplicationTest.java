package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ApplicationTest {
    Application application;

    @BeforeEach
    void setup() {
        application = Application.builder()
                .applicantusername("user")
                .hours(2)
                .grade(1.3)
                .priority(1)
                .lecturer("Tester")
                .role(Role.KORREKTOR)
                .semester("WS2020")
                .module("ProPra")
                .comment("")
                .build();

    }

    @Test
    void TestBuilder() {
        //Arrange in BeforeEach

        assertThat(application)
                .hasFieldOrPropertyWithValue("applicantusername", "user")
                .hasFieldOrPropertyWithValue("hours", 2)
                .hasFieldOrPropertyWithValue("priority", 1)
                .hasFieldOrPropertyWithValue("grade", 1.3)
                .hasFieldOrPropertyWithValue("lecturer", "Tester")
                .hasFieldOrPropertyWithValue("role", Role.KORREKTOR)
                .hasFieldOrPropertyWithValue("semester", "WS2020")
                .hasFieldOrPropertyWithValue("module", "ProPra")
                .hasFieldOrPropertyWithValue("comment", "");


    }

    @Test
    void testEquals() {
        Application application1 = Application.builder()
                .applicantusername("user")
                .hours(2)
                .grade(1.3)
                .priority(1)
                .lecturer("Tester")
                .role(Role.KORREKTOR)
                .semester("WS2020")
                .module("ProPra")
                .comment("")
                .build();

        Application application2 = Application.builder()
                .applicantusername("user")
                .hours(2)
                .grade(1.3)
                .priority(1)
                .lecturer("Tester")
                .role(Role.KORREKTOR)
                .semester("WS2020")
                .module("ProPra")
                .comment("")
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

        assertThat(application.toString()).isEqualTo("Application(applicantusername=user, hours=2, module=ProPra, priority=1, grade=1.3, lecturer=Tester, semester=WS2020, comment=, role=KORREKTOR)");

    }

    @Test
    void testToBuilder() {
        //Arrange in BeforeEach

        Application.ApplicationBuilder applicationBuilder = application.toBuilder();
        Application application2 = applicationBuilder.build();

        assertThat(application2).isEqualTo(application);


    }
}