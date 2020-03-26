package mops.services;

import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import mops.model.classes.Module;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ModuleService.class})
class ModuleServiceTest {


    @MockBean
    ModuleRepository repo;

    @Autowired
    private ModuleService service;

    Module m1;
    List<Module> modules;

    @BeforeEach
    void init() {
        Instant deadline = new Date(System.currentTimeMillis()).toInstant();
        m1 = Module.builder()
                .id(01)
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profName("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("3")
                .seventeenHourLimit("2")
                .hourLimit("10")
                .deadline(deadline)
                .build();


        modules = new ArrayList<>();
        modules.add(m1);

        Mockito.when(repo.findAll()).thenReturn(modules);
        Mockito.when(repo.findDistinctByName(m1.getName())).thenReturn(m1);
    }

    @Test
    void getModulesTest() {
        List<Module> readmodules = service.getModules();

        assertThat(readmodules).isEqualTo(modules);
    }

    @Test
    void findModuleByNameTest() {
        Module module = service.findModuleByName(m1.getName());

        assertThat(module).isEqualTo(module);
    }

    @Test
    void findById() {
    }

    @Test
    void save() {

    }

    @Test
    void deleteModule() {
    }

    @Test
    void deleteAll() {
    }
}