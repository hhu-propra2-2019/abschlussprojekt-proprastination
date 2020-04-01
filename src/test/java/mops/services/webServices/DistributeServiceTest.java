package mops.services.webServices;

import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DistributeServiceTest {

    private DistributeService distributeService = new DistributeService();
    private KeycloakAuthenticationToken tokenMock = mock(KeycloakAuthenticationToken.class);

    @Test
    void getRedirectForRoleOrga() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_orga"));
        when(tokenMock.getAuthorities()).thenReturn(authorities);

        String result = distributeService.getRedirectForRole(tokenMock);

        assertEquals("redirect:/bewerbung2/organisator/", result);
    }

    @Test
    void getRedirectForRoleVerteiler() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_verteiler"));
        when(tokenMock.getAuthorities()).thenReturn(authorities);

        String result = distributeService.getRedirectForRole(tokenMock);

        assertEquals("redirect:/bewerbung2/verteiler/", result);
    }

    @Test
    void getRedirectForRoleSetup() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_setup"));
        when(tokenMock.getAuthorities()).thenReturn(authorities);

        String result = distributeService.getRedirectForRole(tokenMock);

        assertEquals("redirect:/bewerbung2/setup/", result);
    }

    @Test
    void getRedirectForRoleStudentin() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_studentin"));
        when(tokenMock.getAuthorities()).thenReturn(authorities);

        String result = distributeService.getRedirectForRole(tokenMock);

        assertEquals("redirect:/bewerbung2/bewerber/", result);
    }

    @Test
    void getRedirectForRoleUnsupportedRole() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Irgendwas"));
        when(tokenMock.getAuthorities()).thenReturn(authorities);

        String result = distributeService.getRedirectForRole(tokenMock);

        assertEquals("redirect:/error", result);
    }
}