package mops.repositories;

import mops.model.classes.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {

    @Override
    List<Module> findAll();

    /**
     * Finds Module by name
     * @param name
     * @return return single Module
     */
    Module findDistinctByName(String name);

    @Override
    void deleteById(Long aLong);
}
