package mops.repositories;

import mops.model.classes.Application;
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
}
