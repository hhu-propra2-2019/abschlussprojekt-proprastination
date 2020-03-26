package mops.services;

import mops.model.classes.Module;
import mops.model.classes.webclasses.WebModule;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebModuleService.class})
class WebModuleServiceTest {

    @MockBean
    ModuleRepository repo;

    @Autowired
    private WebModuleService service;

    WebModule m1;
    List<WebModule> modules;



    @Test
    void getModules() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteOne() {
    }

    @Test
    void deleteAll() {
    }
}