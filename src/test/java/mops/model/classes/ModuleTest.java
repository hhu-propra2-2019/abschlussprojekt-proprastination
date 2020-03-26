package mops.model.classes;

import mops.model.classes.webclasses.WebModule;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleTest {
    Module module;

    @BeforeEach
    void init() {
        module = Module.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profName("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .hourLimit("20")
                .build();
    }

    @Test
    void toWebModule() {
        WebModule webmodule = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profName("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .hourLimit("20")
                .build();

        assertThat(module.toWebModule()).isEqualTo(webmodule);
    }
}