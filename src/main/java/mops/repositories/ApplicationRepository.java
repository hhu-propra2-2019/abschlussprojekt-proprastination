package mops.repositories;

import mops.model.classes.Application;
import mops.model.classes.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {

    /**
     * Finds application by module and hours
     * @param module the module
     * @return the applications
     */
    List<Application> findByModule(String module);

    @Override
    List<Application> findAll();

    /**
     * Finds all applications for a module
     * @param module the module
     * @return the applications
     */
    List<Application> findAllByModule(Module module);

    /**
     * Finds applications by ID
     * @param id the id
     * @return the application
     */
    Application findById(long id);
}
