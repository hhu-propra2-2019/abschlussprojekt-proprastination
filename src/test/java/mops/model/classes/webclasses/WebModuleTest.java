package mops.model.classes.webclasses;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import mops.model.classes.Module;

import static org.assertj.core.api.Assertions.assertThat;

class WebModuleTest {

    WebModule webmodule;

    @BeforeEach
    void init() {
        webmodule = WebModule.builder()
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
    void toStringArray() {
        String[] output = {"Programmier Praktikum", "ProPra", "Jens", "2", "7", "1", "20"};

        assertThat(webmodule.toStringArray()).isEqualTo(output);
    }

    @Test
    void toModule() {
        Module module = Module.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profName("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("7")
                .seventeenHourLimit("1")
                .hourLimit("20")
                .build();

        assertThat(webmodule.toModule()).isEqualTo(module);
    }
}