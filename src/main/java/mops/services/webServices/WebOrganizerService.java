package mops.services.webServices;

import mops.model.classes.Module;
import mops.model.classes.Organizer;
import mops.services.dbServices.ModuleService;
import mops.services.dbServices.OrganizerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebOrganizerService {

    private final ModuleService moduleService;
    private final OrganizerService organizerService;

    /**
     * Constructor
     * @param moduleService
     * @param organizerService
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
            if (module.getProfSerial().equals(name)) {
                modules.add(module);
            }
        }
        return modules;
    }

    /**
     * Search Organizer by name and if not existing yet, it creates a new one.
     * @param name name of organizer
     * @return organizer
     */
    public Organizer getOrganizerOrNewOrganizer(final String name) {
        Organizer organizer = organizerService.findByUniserial(name);
        if (organizer == null) {
            organizer = Organizer.builder()
                    .uniserial(name)
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
        organizerService.save(Organizer.builder()
                .id(oldOrganizer.getId())
                .uniserial(oldOrganizer.getUniserial())
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
