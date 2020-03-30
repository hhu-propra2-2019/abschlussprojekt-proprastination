package mops.services.webServices;

import mops.model.Account;
import mops.model.classes.Module;
import mops.model.classes.Organizer;
import mops.services.dbServices.ModuleService;
import mops.services.dbServices.OrganizerService;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebOrganizerServiceTest {

    private ModuleService mockModuleService = mock(ModuleService.class);
    private OrganizerService mockOrganizerService = mock(OrganizerService.class);
    private WebOrganizerService webOrganizerService =
            new WebOrganizerService(mockModuleService, mockOrganizerService);

    private Organizer organizer = Organizer.builder()
            .uniserial("neo001")
            .email("neo@matrix.net")
            .name("Thomas Anderson")
            .phonenumber("")
            .build();

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

    @Test
    void getOrganizer() {
        KeycloakAuthenticationToken mockToken = mock(KeycloakAuthenticationToken.class);
        KeycloakPrincipal mockPrincipal = mock(KeycloakPrincipal.class);
        when(mockToken.getPrincipal()).thenReturn(mockPrincipal);
        KeycloakSecurityContext mockContext = mock(KeycloakSecurityContext.class);
        when(mockPrincipal.getKeycloakSecurityContext()).thenReturn(mockContext);
        IDToken mockIDToken = mock(IDToken.class);
        when(mockContext.getIdToken()).thenReturn(mockIDToken);

        Account accountMock = mock(Account.class);

        when(mockOrganizerService.findByUniserial("neo001")).thenReturn(organizer);

        Organizer result = webOrganizerService.getOrganizerOrNewOrganizer("neo001", accountMock, mockToken);

        assertEquals(organizer, result);
    }

    @Test
    void newOrganizer() {
        KeycloakAuthenticationToken mockToken = mock(KeycloakAuthenticationToken.class);
        KeycloakPrincipal mockPrincipal = mock(KeycloakPrincipal.class);
        when(mockToken.getPrincipal()).thenReturn(mockPrincipal);
        KeycloakSecurityContext mockContext = mock(KeycloakSecurityContext.class);
        when(mockPrincipal.getKeycloakSecurityContext()).thenReturn(mockContext);
        IDToken mockIDToken = mock(IDToken.class);
        when(mockContext.getIdToken()).thenReturn(mockIDToken);
        when(mockIDToken.getGivenName()).thenReturn("Thomas");
        when(mockIDToken.getFamilyName()).thenReturn("Anderson");

        Account accountMock = mock(Account.class);
        when(accountMock.getEmail()).thenReturn("neo@matrix.net");

        when(mockOrganizerService.findByUniserial("neo001")).thenReturn(null);

        Organizer result = webOrganizerService.getOrganizerOrNewOrganizer(
                "neo001", accountMock, mockToken);

        assertEquals(organizer, result);
        verify(mockOrganizerService, times(1)).save(result);
    }

    @Test
    void changePhonenumber() {
        Organizer expected = organizer.toBuilder()
                .phonenumber("0987654321")
                .build();

        when(mockOrganizerService.findByUniserial("neo001")).thenReturn(organizer);

        webOrganizerService.changePhonenumber("neo001", "0987654321");

        verify(mockOrganizerService, times(1)).save(expected);
    }

    @Test
    void checkForPhoneNumberOrganizerHasNumber() {
        Organizer organizerMock = mock(Organizer.class);
        when(organizerMock.getPhonenumber()).thenReturn("0123456789");
        when(mockOrganizerService.findByUniserial("serial123")).thenReturn(organizerMock);

        boolean result = webOrganizerService.checkForPhoneNumber("serial123");

        assertTrue(result);
    }

    @Test
    void checkForPhoneNumberOrganizerHasNoNumber() {
        Organizer organizerMock = mock(Organizer.class);
        when(organizerMock.getPhonenumber()).thenReturn("");
        when(mockOrganizerService.findByUniserial("serial123")).thenReturn(organizerMock);

        boolean result = webOrganizerService.checkForPhoneNumber("serial123");

        assertFalse(result);
    }
}