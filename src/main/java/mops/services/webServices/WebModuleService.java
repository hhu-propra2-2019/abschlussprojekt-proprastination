package mops.services.webServices;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.dbServices.ModuleService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableAutoConfiguration
public class WebModuleService {

    private final ModuleService moduleService;

    /**
     * Injects The Service
     *
     * @param moduleService the injected service
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public WebModuleService(final ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Finds all Modules
     *
     * @return List of all Modules
     */
    public List<WebModule> getModules() {
        List<Module> list = moduleService.getModules();
        List<WebModule> webList = new ArrayList<>();
        for (Module m: list) {
            webList.add(toWebModule(m));
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
        moduleService.save(webmodule.toModule());
    }
    /**
     * saves an updated version of Module
     * @param webmodule edited module
     * @param oldName old name of module for finding id
     */
    public void update(final WebModule webmodule, final String oldName) {
        Module m = moduleService.findModuleByName(oldName);
        Module updated = webmodule.toModule();
        updated.setId(m.getId());
        moduleService.save(updated);
    }
    /**
     * Delete single module by name
     * @param name
     */
    public void deleteOne(final String name) {
        Module opt = moduleService.findModuleByName(name);
        moduleService.deleteById(opt.getId());
    }
    /**
     * deletes all modules
     */
    public void deleteAll() {
        moduleService.deleteAll();
    }

    /**
     * Transfer Module into WebModule
     * @param module
     * @return WebModule
     */
    public WebModule toWebModule(final Module module) {
        return WebModule.builder()
                .name(module.getName())
                .shortName(module.getShortName())
                .profSerial(module.getProfSerial())
                .deadlineDate(module.getDeadlineDate())
                .deadlineTime(module.getDeadlineTime())
                .deadline(module.getDeadline())
                .sevenHourLimit(module.getSevenHourLimit())
                .nineHourLimit(module.getNineHourLimit())
                .seventeenHourLimit(module.getSeventeenHourLimit())
                .build();
    }
}
