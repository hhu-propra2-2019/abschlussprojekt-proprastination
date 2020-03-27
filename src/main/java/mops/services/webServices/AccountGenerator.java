package mops.services.webServices;

import mops.model.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class AccountGenerator {

    /**
     * Method for creating an Account
     *
     * @param token The KeycloakAuthentication
     * @return The Users Account
     */
    public static Account createAccountFromPrincipal(final KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

}
