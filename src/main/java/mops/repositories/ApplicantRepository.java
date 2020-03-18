package mops.repositories;

import mops.model.classes.Applicant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Long> {
    Applicant findByFirstName(String name);

    @Override
    List<Applicant> findAll();

    List<Applicant> findByUniserial(String uniserial);
}
