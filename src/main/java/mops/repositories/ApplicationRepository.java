package mops.repositories;

import mops.model.classes.Application;
import mops.model.classes.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    List<Application> findAllByModule(Module module);
    Application findById(long id);
    List<Application> findAllById(Module module);
}
