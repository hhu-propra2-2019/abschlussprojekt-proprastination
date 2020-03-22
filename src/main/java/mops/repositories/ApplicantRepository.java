package mops.repositories;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
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
     * Finds Applicant with given uniserial
     *
     * @param uniserial Unikennung
     * @return the applicant
     */
    Applicant findByUniserial(String uniserial);

    /**
     * Finds Applicant by Application
     * @param application application
     * @return applicant
     */
    Applicant findByApplications(Application application);
}
