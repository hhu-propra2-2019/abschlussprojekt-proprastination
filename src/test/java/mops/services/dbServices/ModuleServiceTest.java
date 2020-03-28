package mops.services.dbServices;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModuleServiceTest {

    private ModuleRepository repositoryMock = mock(ModuleRepository.class);
    private ModuleService moduleService = new ModuleService(repositoryMock);

    Module module = Module.builder()
            .name("Kung Fu")
            .shortName("KF")
            .profSerial("neo001")
            .deadline(Instant.parse("1970-01-01T00:00:00Z"))
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