package mops.services.dbServices;

import mops.model.classes.Module;
import mops.repositories.ModuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    /**
     * Injects The Repository
     *
     * @param moduleRepository the injected repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ModuleService(final ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * Finds all Modules
     *
     * @return List of all Modules
     */
    public List<Module> getModules() {
        return moduleRepository.findAll();
    }

    /**
     * Finds Module by Name.
     *
     * @param name Name of Modul.
     * @return Distinct Module.
     */
    public Module findModuleByName(final String name) {
        return moduleRepository.findDistinctByName(name);
    }

    /**
     * Finds Module by id.
     *
     * @param id id.
     * @return Distinct Module.
     */
    public Module findById(final long id) {
        return moduleRepository.findById(id).get();
    }

    /**
     * Saves a module
     *
     * @param module module.
     */
    public void save(final Module module) {
        module.setDeadline(LocalDateTime.parse((String.format("%sT%s:00",
                module.getDeadlineDate(), module.getDeadlineTime()))));
        moduleRepository.save(module);
    }

    /**
     * delete single module
     *
     * @param name of the module to be deleted
     */
    public void deleteModule(final String name) {
        Module m = moduleRepository.findDistinctByName(name);
        moduleRepository.deleteById(m.getId());
    }

    /**
     * Deletes all.
     */
    public void deleteAll() {
        moduleRepository.deleteAll();
    }

    /**
     * -
     * @param id
     */
    public void deleteById(final long id) {
        moduleRepository.deleteById(id);
    }

    /**
     * -
     * @return -
     */
    public List<String> getModuleNames() {
        return moduleRepository.findAll().stream().map(Module::getName).collect(Collectors.toList());
    }
}
