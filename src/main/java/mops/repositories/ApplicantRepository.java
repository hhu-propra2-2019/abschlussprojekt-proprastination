package mops.repositories;

import mops.model.classes.Applicant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Long> {

    /**
     * Finds all Applicants
     *
     * @return List of Applicants
     */
    @Override
    List<Applicant> findAll();

    /**
     * Finds all Applicants with give uniserial
     *
     * @param uniserial Unikennung
     * @return List of Applicants
     */
    Applicant findByUniserial(String uniserial);
}
