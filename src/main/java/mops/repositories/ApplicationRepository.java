package mops.repositories;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    /**
     * Finds all application of an applicant
     * @param applicant the applicant
     * @return his applications
     */
    List<Application> findAllByApplicant(Applicant applicant);

    /**
     * Finds application by applicant and module
     * @param applicant the applicant
     * @param module the module he applied in
     * @return the application
     */
    Application findByApplicantAndModule(Applicant applicant, String module);
}
