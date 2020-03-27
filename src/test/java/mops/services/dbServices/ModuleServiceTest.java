package mops.services.dbServices;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ModuleServiceTest {

    private ModuleRepository repositoryMock = mock(ModuleRepository.class);
    private ModuleService moduleService = new ModuleService(repositoryMock);

    @Test
    void toWebModule() {
        Module module = new Module();
        module.setName("Kung Fu");
        module.setShortName("KF");
        module.setProfSerial("neo001");
        module.setDeadline(Instant.parse("1970-01-01T00:00:00Z"));
        module.setSevenHourLimit("70");
        module.setNineHourLimit("90");
        module.setSeventeenHourLimit("170");

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
}