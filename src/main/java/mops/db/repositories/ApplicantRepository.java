package mops.db.repositories;

import mops.model.classes.Applicant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Long> {
    Applicant findByName(String name);
}
