package mops.services.webServices;

import mops.model.classes.Module;
import mops.services.dbServices.ModuleService;
import mops.services.dbServices.OrganizerService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebOrganizerServiceTest {

    ModuleService mockModuleService = mock(ModuleService.class);
    OrganizerService mockOrganizerService = mock(OrganizerService.class);
    private WebOrganizerService webOrganizerService =
            new WebOrganizerService(mockModuleService, mockOrganizerService);

    @Test
    void getOrganizerModulesByName() {
        List<Module> modules = new ArrayList<>();
        when(mockModuleService.getModules()).thenReturn(modules);

        Module correctProfessor1 = mock(Module.class);
        when(correctProfessor1.getProfSerial()).thenReturn("neo001");
        modules.add(correctProfessor1);

        Module incorrectProfessor1 = mock(Module.class);
        when(incorrectProfessor1.getProfSerial()).thenReturn("jabon007");
        modules.add(incorrectProfessor1);

        Module correctProfessor2 = mock(Module.class);
        when(correctProfessor2.getProfSerial()).thenReturn("neo001");
        modules.add(correctProfessor2);

        Module incorrectProfessor2 = mock(Module.class);
        when(incorrectProfessor2.getProfSerial()).thenReturn("random000");
        modules.add(incorrectProfessor2);

        List<Module> result = webOrganizerService.getOrganizerModulesByName("neo001");

        assertThat(result).containsOnly(correctProfessor1, correctProfessor2);
    }

    @Test
    void getOrganizerModulesByNameForNoModules() {
        List<Module> modules = new ArrayList<>();
        when(mockModuleService.getModules()).thenReturn(modules);

        List<Module> result = webOrganizerService.getOrganizerModulesByName("neo001");

        assertThat(result).hasSize(0);
    }

    /*@Test
    void getOrganizerOrNewOrganizer() {
    }

    @Test
    void changePhonenumber() {
    }

    @Test
    void checkForPhoneNumber() {
    }*/
}