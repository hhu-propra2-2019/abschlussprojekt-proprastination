package mops.services;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.WebModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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
    ModuleService moduleService;

    @Autowired
    @InjectMocks
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

        module = Module.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("3")
                .seventeenHourLimit("2")
                .build();

        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);


        Mockito.when(moduleService.getModules()).thenReturn(moduleList);
        Mockito.when(moduleService.findModuleByName(anyString())).thenReturn(module);
        Mockito.when(moduleService.findById(module.getId())).thenReturn(module);
        Mockito.when(moduleService.toWebModule(module)).thenReturn(webmodule);
    }

    @Test
    void getModulesTest() {
        List<WebModule> webModulesList = service.getModules();

        assertThat(webModulesList).isEqualTo(modules);
    }

    @Test
    void save() {
        service.save(webmodule);

        verify(moduleService, times(1)).save(service.toModule(webmodule));
    }

    @Test
    void update() {
        webmodule.setName("Zauberei");

        service.update(webmodule, "Programmier Praktikum");

        verify(moduleService, times(1)).findModuleByName("Programmier Praktikum");
        verify(moduleService, times(1)).save(any(Module.class));
    }

    @Test
    void deleteOne() {
        service.deleteOne(webmodule.getName());

        verify(moduleService, times(1)).deleteById(module.getId());
    }

    @Test
    void deleteAll() {
        service.deleteAll();

        verify(moduleService, times(1)).deleteAll();
    }

    @Test
    void toModule() {
        Module module = Module.builder()
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("3")
                .seventeenHourLimit("2")
                .build();
        Module m = service.toModule(webmodule);
        assertThat(m).isEqualTo(module);
    }
}