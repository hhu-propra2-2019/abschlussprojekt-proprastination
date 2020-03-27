package mops.services;

import mops.model.classes.Module;
import mops.model.classes.Organizer;
import mops.repositories.OrganizerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizerService {

    private final OrganizerRepository organizerRepository;
    private final ModuleService moduleService;

    /**
     * Injects The Repository
     *
     * @param organizerRepository the injected repository
     * @param moduleService the injected moduleService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrganizerService(final OrganizerRepository organizerRepository, final ModuleService moduleService) {
        this.organizerRepository = organizerRepository;
        this.moduleService = moduleService;
    }

    /**
     * returns all Organizers
     * @return List of all Organizer
     */
    public List<Organizer> findAll() {
        return organizerRepository.findAll();
    }

    /**
     * returns organizer with given uniserial
     * @param uniserial uniserial
     * @return Organizer
     */
    public Organizer findByUniserial(final String uniserial) {
        return organizerRepository.findOrganizerByUniserial(uniserial);
    }

    /**
     * saves or updates organizer
     * @param organizer organizer
     */
    public void save(final Organizer organizer) {
        organizerRepository.save(organizer);
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
        Organizer organizer = findByUniserial(name);
        if (organizer == null) {
            organizer = Organizer.builder()
                    .uniserial(name)
                    .phonenumber("Bitte speichern Sie Ihre Telefonnummer!")
                    .build();
            save(organizer);
        }
        return organizer;
    }

    /**
     * updates the phonenumber of an Organizer
     * @param name name of organizer
     * @param phone new phonenumber
     */
    public void changePhonenumber(final String name, final String phone) {
        Organizer oldOrganizer = findByUniserial(name);
        save(Organizer.builder()
                .id(oldOrganizer.getId())
                .uniserial(oldOrganizer.getUniserial())
                .phonenumber(phone)
                .build());
    }
}
