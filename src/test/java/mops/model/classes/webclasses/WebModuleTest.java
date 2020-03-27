package mops.model.classes.webclasses;

import org.bouncycastle.math.raw.Mod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mops.model.classes.Module;

import static org.assertj.core.api.Assertions.assertThat;

class WebModuleTest {

    WebModule webmodule;

    @BeforeEach
    void init() {
        webmodule = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .build();

    }

    @Test
    void allArgsConstructorTest() {
        WebModule full = new WebModule("Magische Tränke", "MaTrä",
                "Maggie Smith", "3",
                "4", "5");

        assertThat(full.getName()).isEqualTo("Magische Tränke");
        assertThat(full.getShortName()).isEqualTo("MaTrä");
        assertThat(full.getProfSerial()).isEqualTo("Maggie Smith");
        assertThat(full.getSevenHourLimit()).isEqualTo("3");
        assertThat(full.getNineHourLimit()).isEqualTo("4");
        assertThat(full.getSeventeenHourLimit()).isEqualTo("5");
    }

    @Test
    void noArgsConstructorTest() {
        WebModule empty = new WebModule();

        assertThat(empty.getName()).isNull();
        assertThat(empty.getShortName()).isNull();
        assertThat(empty.getProfSerial()).isNull();
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
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .build();

        assertThat(webmodule.hashCode()).isEqualTo(webmodule2.hashCode());
    }

    @Test
    void testToString() {
        assertThat(webmodule.toString()).isEqualTo("WebModule(name=Programmier Praktikum, "+
                "shortName=ProPra, profSerial=Jens, " +
                "sevenHourLimit=2, nineHourLimit=7, " +
                "seventeenHourLimit=1)");
    }
}