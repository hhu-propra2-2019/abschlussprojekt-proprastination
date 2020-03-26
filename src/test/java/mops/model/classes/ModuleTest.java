package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ModuleTest {

    private Module module;

    @BeforeEach
    void setUp() {
        module = Module.builder()
                .name("Introduction to Logic")
                .deadline(Instant.parse("2018-11-30T18:35:24.00Z"))
                .shortName("Logic")
                .profName("Hershel Layton")
                .sevenHourLimit("7")
                .nineHourLimit("9")
                .seventeenHourLimit("17")
                .hourLimit("42")
                .build();
    }

    @Test
    void testBuilder() {
        Instant expectedDeadline = Instant.parse("2018-11-30T18:35:24.00Z");

        assertThat(module)
                .hasFieldOrPropertyWithValue("name", "Introduction to Logic")
                .hasFieldOrPropertyWithValue("deadline", expectedDeadline)
                .hasFieldOrPropertyWithValue("shortName", "Logic")
                .hasFieldOrPropertyWithValue("profName", "Hershel Layton")
                .hasFieldOrPropertyWithValue("sevenHourLimit", "7")
                .hasFieldOrPropertyWithValue("nineHourLimit", "9")
                .hasFieldOrPropertyWithValue("seventeenHourLimit", "17")
                .hasFieldOrPropertyWithValue("hourLimit", "42");
    }

    @Test
    void testEquals() {
        Module newModule = Module.builder()
                .name("Introduction to Logic")
                .deadline(Instant.parse("2018-11-30T18:35:24.00Z"))
                .shortName("Logic")
                .profName("Hershel Layton")
                .sevenHourLimit("7")
                .nineHourLimit("9")
                .seventeenHourLimit("17")
                .hourLimit("42")
                .build();

        assertEquals(module, newModule);
        assertEquals(newModule, module);
    }

    @Test
    void testEqualsNotEqual() {
        Module newModule = Module.builder()
                .name("Introduction to Logic")
                .deadline(Instant.parse("2018-11-30T18:35:24.00Z"))
                .shortName("Logic")
                .profName("Hershel Layton")
                .sevenHourLimit("7")
                .nineHourLimit("9")
                .seventeenHourLimit("17")
                .hourLimit("777")
                .build();

        assertNotEquals(module, newModule);
        assertNotEquals(newModule, module);
    }

    @Test
    void testHashCode() {
        Module newModule = Module.builder()
                .name("Introduction to Logic")
                .deadline(Instant.parse("2018-11-30T18:35:24.00Z"))
                .shortName("Logic")
                .profName("Hershel Layton")
                .sevenHourLimit("7")
                .nineHourLimit("9")
                .seventeenHourLimit("17")
                .hourLimit("42")
                .build();

        int hashCode = module.hashCode();
        int newHashCode = newModule.hashCode();

        assertEquals(hashCode, newHashCode);
    }

    @Test
    void testHashCodeNotEqual() {
        Module newModule = Module.builder()
                .name("Introduction to Puzzle Solving")
                .deadline(Instant.parse("2018-11-30T18:35:24.00Z"))
                .shortName("Logic")
                .profName("Hershel Layton")
                .sevenHourLimit("7")
                .nineHourLimit("9")
                .seventeenHourLimit("17")
                .hourLimit("42")
                .build();

        int hashCode = module.hashCode();
        int newHashCode = newModule.hashCode();

        assertNotEquals(hashCode, newHashCode);
    }

}