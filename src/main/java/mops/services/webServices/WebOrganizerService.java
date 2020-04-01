package mops.services.webServices;

import mops.model.Account;
import mops.model.classes.Module;
import mops.model.classes.Organizer;
import mops.model.classes.Organizer.OrganizerBuilder;
import mops.services.dbServices.ModuleService;
import mops.services.dbServices.OrganizerService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebOrganizerService {

    private final ModuleService moduleService;
    private final OrganizerService organizerService;

    /**
     * Constructor
     *
     * @param moduleService    ModuleService
     * @param organizerService OrganizerService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebOrganizerService(final ModuleService moduleService, final OrganizerService organizerService) {
        this.moduleService = moduleService;
        this.organizerService = organizerService;
    }

    /**
     * Finds all Modules of the Organizer
     * @param name name of organizer
     * @return List of Modules the organizer is in charge of
     */
    public List<Module> getOrganizerModulesByName(final String name) {
        List<Module> modules = new ArrayList<>();
        for (Module module : moduleService.getModules()) {
            if (module.getProfSerial().equals(name) && LocalDateTime.now().isBefore(module.getOrgaDeadline())) {
                modules.add(module);
            }
        }
        return modules;
    }

    /**
     * Search Organizer by name and if not existing yet, it creates a new one.
     *
     * @param name    name of organizer
     * @param account kycloak account
     * @param token   keycloaktoken
     * @return organizer
     */
    public Organizer getOrganizerOrNewOrganizer(final String name, final Account account,
                                                final KeycloakAuthenticationToken token) {
        Organizer organizer = organizerService.findByUniserial(name);
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        IDToken idToken = principal.getKeycloakSecurityContext().getIdToken();
        if (organizer == null) {
            organizer = Organizer.builder()
                    .uniserial(name)
                    .email(account.getEmail())
                    .name(idToken.getGivenName() + " " + idToken.getFamilyName())
                    .phonenumber("")
                    .build();
            organizerService.save(organizer);
        }
        return organizer;
    }

    /**
     * updates the phonenumber of an Organizer
     * @param name name of organizer
     * @param phone new phonenumber
     */
    public void changePhonenumber(final String name, final String phone) {
        Organizer oldOrganizer = organizerService.findByUniserial(name);
        OrganizerBuilder organizerBuilder = oldOrganizer.toBuilder();
        organizerService.save(organizerBuilder
                .phonenumber(phone)
                .build());
    }

    /**
     * checks for Organizer phoneNumber
     * @param name name of organizer
     * @return true, if phone number is set, false if not
     */
    public boolean checkForPhoneNumber(final String name) {
        Organizer organizer = organizerService.findByUniserial(name);
        return !"".equals(organizer.getPhonenumber());
    }
}
