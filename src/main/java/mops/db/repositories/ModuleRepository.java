package mops.db.repositories;

import mops.model.classes.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
    @Override
    List<Module> findAll();
}