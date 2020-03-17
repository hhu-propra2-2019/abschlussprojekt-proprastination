package mops.db.repositories;

import mops.model.classes.Application;
import org.springframework.data.repository.CrudRepository;


public interface ApplicationRepository extends CrudRepository<Application, Long> {

}
