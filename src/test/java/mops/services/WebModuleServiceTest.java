package mops.services;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebModuleService.class})
class WebModuleServiceTest {

    @MockBean
    ModuleRepository repo;

    @Autowired
    private WebModuleService service;

    WebModule webmodule;
    List<WebModule> modules;
    Module module;

    @BeforeEach
    void init() {
        webmodule = WebModule.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("3")
                .seventeenHourLimit("2")
                .build();




        modules = new ArrayList<>();
        modules.add(webmodule);

        Instant deadline = new Date(System.currentTimeMillis()).toInstant();
        module = webmodule.toModule();
        module.setId(01);
        //module.setDeadline(deadline);

        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);


        Mockito.when(repo.findAll()).thenReturn(moduleList);
        Mockito.when(repo.findDistinctByName(anyString())).thenReturn(module);
        Mockito.when(repo.findById(module.getId())).thenReturn(java.util.Optional.ofNullable(module));
    }

    @Test
    void getModulesTest() {
        List<WebModule> webModulesList = service.getModules();

        assertThat(webModulesList).isEqualTo(modules);
    }

    @Test
    void save() {
        service.save(webmodule);

        verify(repo, times(1)).save(webmodule.toModule());
    }

    @Test
    void update() {
        webmodule.setName("Zauberei");

        service.update(webmodule, "Programmier Praktikum");
        List<WebModule> readModules = service.getModules();

        verify(repo, times(1)).findDistinctByName("Programmier Praktikum");
        verify(repo, times(1)).save(any(Module.class));
    }

    @Test
    void deleteOne() {
        service.deleteOne(webmodule.getName());

        verify(repo, times(1)).deleteById(module.getId());
    }

    @Test
    void deleteAll() {
        service.deleteAll();

        verify(repo, times(1)).deleteAll();
    }
}