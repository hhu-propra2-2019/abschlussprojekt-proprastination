package mops.services.dbServices;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModuleServiceTest {

    private ModuleRepository repositoryMock = mock(ModuleRepository.class);
    private ModuleService moduleService = new ModuleService(repositoryMock);

    Module module = Module.builder()
            .name("Kung Fu")
            .shortName("KF")
            .profSerial("neo001")
            .deadline(LocalDateTime.parse("1970-01-01T00:00:00.000"))
            .sevenHourLimit("70")
            .nineHourLimit("90")
            .seventeenHourLimit("170")
            .build();

    @Test
    void toWebModule() {
        WebModule expected = new WebModule();
        expected.setName("Kung Fu");
        expected.setShortName("KF");
        expected.setProfSerial("neo001");
        expected.setSevenHourLimit("70");
        expected.setNineHourLimit("90");
        expected.setSeventeenHourLimit("170");
        expected.setDeadline(LocalDateTime.parse("1970-01-01T00:00:00.000"));

        WebModule result = moduleService.toWebModule(module);

        assertEquals(expected, result);
    }

    @Test
    void deleteModule() {
        when(repositoryMock.findDistinctByName("KungFu")).thenReturn(module);

        moduleService.deleteModule("KungFu");

        verify(repositoryMock, times(1)).deleteById(module.getId());
    }
}