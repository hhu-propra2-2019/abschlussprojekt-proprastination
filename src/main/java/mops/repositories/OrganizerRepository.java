package mops.repositories;

import mops.model.classes.Organizer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizerRepository extends CrudRepository<Organizer, Long> {

    /**
     * Returns all Organizers as List instead of iterable.
     *
     * @return List.
     */
    @Override
    @NonNull
    List<Organizer> findAll();

    /**
     * Returns Organizer with given uniserial.
     * @param uniserial uniserial
     * @return Organizer
     */
    Organizer findOrganizerByUniserial(String uniserial);
}
