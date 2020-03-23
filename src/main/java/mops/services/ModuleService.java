package mops.services;

import mops.model.classes.Module;
import mops.repositories.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
        List<Module> modules = new LinkedList<>();
        List<String> strings = CSVService.getModules();
        for (int i = 0; i < strings.size(); i++) {
            modules.add(Module.builder()
                    .name(strings.get(i))
                    .id(UUID.randomUUID())
                    .build());
        }
        return modules;
    }
}
