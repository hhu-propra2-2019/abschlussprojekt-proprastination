package mops.services.webServices;

import mops.model.Account;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountGeneratorTest {

    @Test
    void createAccountFromPrincipal() {
        KeycloakAuthenticationToken tokenMock = mock(KeycloakAuthenticationToken.class);

        KeycloakPrincipal principalMock = mock(KeycloakPrincipal.class);
        when(tokenMock.getPrincipal()).thenReturn(principalMock);
        when(principalMock.getName()).thenReturn("Muster Name");

        KeycloakSecurityContext contextMock = mock(KeycloakSecurityContext.class);
        when(principalMock.getKeycloakSecurityContext()).thenReturn(contextMock);
        IDToken idTokenMock = mock(IDToken.class);
        when(contextMock.getIdToken()).thenReturn(idTokenMock);
        when(idTokenMock.getEmail()).thenReturn("muster@mail.net");

        OidcKeycloakAccount keycloakAccountMock = mock(OidcKeycloakAccount.class);
        when(tokenMock.getAccount()).thenReturn(keycloakAccountMock);
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_studentin");
        roles.add("ROLE_orga");
        when((keycloakAccountMock.getRoles())).thenReturn(roles);

        Account result = AccountGenerator.createAccountFromPrincipal(tokenMock);

        assertThat(result).hasFieldOrPropertyWithValue("name", "Muster Name");
        assertThat(result).hasFieldOrPropertyWithValue("email", "muster@mail.net");
        assertThat(result).hasFieldOrPropertyWithValue("image", null);
        assertThat(result).hasFieldOrPropertyWithValue("roles", roles);
    }
}