package mops.model.classes;

import mops.model.classes.webclasses.WebModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleTest {
    Module module;
    Instant deadline;

    @BeforeEach
    void init() {
        deadline = new Date(System.currentTimeMillis()).toInstant();
        module = Module.builder()
                .id(01)
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .deadline(deadline)
                .build();
    }

    @Test
    void toWebModule() {
        WebModule webmodule = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .build();

        assertThat(module.toWebModule()).isEqualTo(webmodule);
    }

    @Test
    void setId() {
        module.setId(02);

        assertThat(module.getId()).isEqualTo(02);
    }

    @Test
    void testEquals() {
        Module m2 = Module.builder()
                .id(01)
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .deadline(deadline)
                .build();

        assertThat(module.equals(m2)).isTrue();
    }

    @Test
    void testHashCode() {
        Module m2 = Module.builder()
                .id(01)
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .deadline(deadline)
                .build();

        assertThat(module.hashCode()).isEqualTo(m2.hashCode());
    }

    @Test
    void getId() {

        assertThat(module.getId()).isEqualTo(01);
    }

    @Test
    void testToString() {
        String d = module.getDeadline().toString();
        assertThat(module.toString()).isEqualTo("Module(name=Programmier Praktikum, deadline=" +
                d +
                ", shortName=ProPra, profSerial=Jens, " +
                "sevenHourLimit=2, nineHourLimit=7, " +
                "seventeenHourLimit=1)");
    }

    @Test
    void noArgsContructor() {
        Module empty = new Module();

        assertThat(empty.getId()).isEqualTo(0L);
        assertThat(empty.getName()).isNull();
        assertThat(empty.getShortName()).isNull();
        assertThat(empty.getProfSerial()).isNull();
        assertThat(empty.getSevenHourLimit()).isNull();
        assertThat(empty.getNineHourLimit()).isNull();
        assertThat(empty.getSeventeenHourLimit()).isNull();
        assertThat(empty.getDeadline()).isNull();
    }

    @Test
    void allArgsConstructor() {
        Module full = new Module(module.getId(),
                                module.getName(),
                                module.getDeadline(),
                                module.getShortName(),
                                module.getProfSerial(),
                                module.getSevenHourLimit(),
                                module.getNineHourLimit(),
                                module.getSeventeenHourLimit());



        assertThat(full.getId()).isEqualTo(module.getId());
        assertThat(full.getName()).isEqualTo(module.getName());
        assertThat(full.getShortName()).isEqualTo(module.getShortName());
        assertThat(full.getProfSerial()).isEqualTo(module.getProfSerial());
        assertThat(full.getSevenHourLimit()).isEqualTo(module.getSevenHourLimit());
        assertThat(full.getNineHourLimit()).isEqualTo(module.getNineHourLimit());
        assertThat(full.getSeventeenHourLimit()).isEqualTo(module.getSeventeenHourLimit());
        assertThat(full.getDeadline()).isEqualTo(module.getDeadline());

    }
}