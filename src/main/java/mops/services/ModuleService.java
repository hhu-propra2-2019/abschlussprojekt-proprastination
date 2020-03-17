package mops.services;

import mops.db.repositories.ModuleRepository;
import mops.model.classes.Module;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(final ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> getModules() {
        return moduleRepository.findAll();
    }
}