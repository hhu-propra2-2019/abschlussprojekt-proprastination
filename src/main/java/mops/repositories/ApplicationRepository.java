package mops.repositories;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    List<Application> findAllByApplicant(Applicant applicant);
    Application findByApplicantAndModule(Applicant applicant, String module);
}
