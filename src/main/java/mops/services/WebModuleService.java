package mops.services;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.repositories.ModuleRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableAutoConfiguration
public class WebModuleService {

    private final ModuleRepository moduleRepository;

    /**
     * Injects The Repository
     *
     * @param moduleRepository the injected repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebModuleService(final ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * Finds all Modules
     *
     * @return List of all Modules
     */
    public List<WebModule> getModules() {
        List<Module> list = moduleRepository.findAll();
        List<WebModule> webList = new ArrayList<>();
        for (Module m: list) {
            webList.add(m.toWebModule());
        }
        return webList;
    }

    /**
     * Saves a module
     *
     * @param webmodule module.
     */
    public void save(final WebModule webmodule) {
        webmodule.setDeadline(LocalDateTime.parse((String.format("%sT%s:00",
                webmodule.getDeadlineDate(), webmodule.getDeadlineTime()))));
        moduleRepository.save(webmodule.toModule());
    }
    /**
     * saves an updated version of Module
     * @param webmodule edited module
     * @param oldName old name of module for finding id
     */
    public void update(final WebModule webmodule, final String oldName) {
        Module m = moduleRepository.findDistinctByName(oldName);
        Module updated = webmodule.toModule();
        updated.setId(m.getId());
        moduleRepository.save(updated);
    }
    /**
     * Delete single module by name
     * @param name
     */
    public void deleteOne(final String name) {
        Module opt = moduleRepository.findDistinctByName(name);
        moduleRepository.deleteById(opt.getId());
    }
    /**
     * deletes all modules
     */
    public void deleteAll() {
        moduleRepository.deleteAll();
    }
}
