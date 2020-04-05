package mops.services.dbServices;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
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
        moduleRepository.save(module);
    }

    /** Sets the deadline (This should me reworked)
     *
     * @param module current module
     */
    private void concatenateDeadlines(final Module module) {
        if (module.getApplicantDeadlineDate().isEmpty() || module.getApplicantDeadlineTime().isEmpty()
        || module.getOrgaDeadlineDate().isEmpty() || module.getOrgaDeadlineTime().isEmpty()) {
            return;
        }
        module.setApplicantDeadline(LocalDateTime.parse((String.format("%sT%s:00",
                module.getApplicantDeadlineDate(), module.getApplicantDeadlineTime()))));

        module.setOrgaDeadline(LocalDateTime.parse((String.format("%sT%s:00",
                module.getOrgaDeadlineDate(), module.getOrgaDeadlineTime()))));
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
                .applicantDeadlineDate(module.getApplicantDeadlineDate())
                .applicantDeadlineTime(module.getApplicantDeadlineTime())
                .applicantDeadline(module.getApplicantDeadline())
                .orgaDeadlineDate(module.getOrgaDeadlineDate())
                .orgaDeadlineTime(module.getOrgaDeadlineTime())
                .orgaDeadline(module.getOrgaDeadline())
                .sevenHourLimit(module.getSevenHourLimit())
                .nineHourLimit(module.getNineHourLimit())
                .seventeenHourLimit(module.getSeventeenHourLimit())
                .build();
    }
}
