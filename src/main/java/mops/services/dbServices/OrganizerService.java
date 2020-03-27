package mops.services.dbServices;

import mops.model.classes.Organizer;
import mops.repositories.OrganizerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizerService {

    private final OrganizerRepository organizerRepository;

    /**
     * Injects The Repository
     *
     * @param organizerRepository the injected repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrganizerService(final OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
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


}
