package mops.model.classes.webclasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WebModuleTest {

    WebModule webmodule;
    LocalDateTime deadline = LocalDateTime.now();
    LocalDateTime deadline2 = LocalDateTime.now();

    @BeforeEach
    void init() {
        webmodule = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .applicantDeadlineDate("2020-01-01")
                .applicantDeadlineTime("09:41")
                .applicantDeadline(deadline)
                .orgaDeadlineDate("2020-01-01")
                .orgaDeadlineTime("09:42")
                .orgaDeadline(deadline2)
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .build();

    }

 /* Brauchen wir diesen Test überhaupt? Es existiert doch nicht mal ein Konstruktor :D
    @Test
    void allArgsConstructorTest() {
        LocalDateTime deadline = LocalDateTime.now();
        WebModule full = new WebModule("Magische Tränke", "MaTrä", "Maggie Smith", "2020-01-01", "09:41", deadline, "3", "4", "5");

        assertThat(full.getName()).isEqualTo("Magische Tränke");
        assertThat(full.getShortName()).isEqualTo("MaTrä");
        assertThat(full.getProfSerial()).isEqualTo("Maggie Smith");
        assertThat(full.getApplicantDeadlineDate()).isEqualTo("2020-01-01");
        assertThat(full.getApplicantDeadlineTime()).isEqualTo("09:41");
        assertThat(full.getApplicantDeadline()).isEqualTo(deadline);
        assertThat(full.getSevenHourLimit()).isEqualTo("3");
        assertThat(full.getNineHourLimit()).isEqualTo("4");
        assertThat(full.getSeventeenHourLimit()).isEqualTo("5");
    }*/

    @Test
    void noArgsConstructorTest() {
        WebModule empty = new WebModule();

        assertThat(empty.getName()).isNull();
        assertThat(empty.getShortName()).isNull();
        assertThat(empty.getProfSerial()).isNull();
        assertThat(empty.getApplicantDeadlineDate()).isNull();
        assertThat(empty.getApplicantDeadlineTime()).isNull();
        assertThat(empty.getApplicantDeadline()).isNull();
        assertThat(empty.getOrgaDeadlineDate()).isNull();
        assertThat(empty.getOrgaDeadlineTime()).isNull();
        assertThat(empty.getOrgaDeadline()).isNull();
        assertThat(empty.getSevenHourLimit()).isNull();
        assertThat(empty.getNineHourLimit()).isNull();
        assertThat(empty.getSeventeenHourLimit()).isNull();
    }

    @Test
    void setNameTest() {
        webmodule.setName("Zauberei");

        assertThat(webmodule.getName()).isEqualTo("Zauberei");
    }

    @Test
    void testEquals() {
        WebModule webmodule2 = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .applicantDeadlineDate("2020-01-01")
                .applicantDeadlineTime("09:41")
                .applicantDeadline(deadline)
                .orgaDeadlineDate("2020-01-01")
                .orgaDeadlineTime("09:42")
                .orgaDeadline(deadline2)
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .build();

        assertThat(webmodule).isEqualTo(webmodule2);
    }

    @Test
    void testHashCode() {
        WebModule webmodule2 = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .applicantDeadlineDate("2020-01-01")
                .applicantDeadlineTime("09:41")
                .applicantDeadline(deadline)
                .orgaDeadlineDate("2020-01-01")
                .orgaDeadlineTime("09:42")
                .orgaDeadline(deadline2)
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .build();

        assertThat(webmodule.hashCode()).isEqualTo(webmodule2.hashCode());
    }

    @Test
    void testToString() {
        String deadline = webmodule.getApplicantDeadline().toString();
        String deadline2 = webmodule.getOrgaDeadline().toString();
        assertThat(webmodule.toString()).isEqualTo("WebModule(name=Programmier Praktikum, "+
                "shortName=ProPra, profSerial=Jens, applicantDeadlineDate=2020-01-01, " +
                "applicantDeadlineTime=09:41, applicantDeadline=" + deadline + ", orgaDeadlineDate=2020-01-01, "
                + "orgaDeadlineTime=09:42, orgaDeadline=" + deadline2 +
                ", sevenHourLimit=2, " +
                "nineHourLimit=7, seventeenHourLimit=1)");
    }
}