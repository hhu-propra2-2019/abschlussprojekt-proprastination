package mops.repositories;

import mops.model.classes.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
    /**
     * Returns all Modules as List instead of iterable.
     *
     * @return List.
     */
    @Override
    @NonNull
    List<Module> findAll();

    /**
     * Returns Module given the name as search parameter.
     *
     * @param name name of module
     * @return Module.
     */
    Module findDistinctByName(String name);

    /**
     * Delete single Module by id
     * @param id
     */
    void deleteById(Long id);

    /**
     * delete All entries
     */
    void deleteAll();
}
