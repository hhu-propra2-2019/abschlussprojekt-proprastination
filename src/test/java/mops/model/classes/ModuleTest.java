package mops.model.classes;

import mops.model.classes.webclasses.WebModule;
import org.junit.Before;
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
}