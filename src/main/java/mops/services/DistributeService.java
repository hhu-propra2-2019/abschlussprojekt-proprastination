package mops.services;

import mops.model.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DistributeService {

    private boolean checkRole(final KeycloakAuthenticationToken token, final String role) {
        for (GrantedAuthority auth : Objects.requireNonNull(token).getAuthorities()) {
            if (auth.equals(new SimpleGrantedAuthority(role))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for creating an Account
     *
     * @param token The KeycloakAuthentication
     * @return The Users Account
     */
    public Account createAccountFromPrincipal(final KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    /**
     * returns the redirect url depending on the role the user has
     * @param token Keycloak auth token
     * @return redirect url
     */
    public String getRedirectForRole(final KeycloakAuthenticationToken token) {
        if (checkRole(token, "ROLE_orga")) {
            return "redirect:/bewerbung2/organisator/";
        }
        if (checkRole(token, "ROLE_verteiler")) {
            return "redirect:/bewerbung2/verteiler/";
        }
        if (checkRole(token, "ROLE_setup")) {
            return "redirect:/bewerbung2/setup/";
        }
        if (checkRole(token, "ROLE_studentin")) {
            return "redirect:/bewerbung2/bewerber/";
        }
        return "redirect:/error";
    }
}
