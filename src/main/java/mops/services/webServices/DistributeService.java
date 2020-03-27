package mops.services.webServices;

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
