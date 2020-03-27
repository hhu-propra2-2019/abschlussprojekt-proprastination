package mops.repositories;

import mops.model.classes.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class ModuleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ModuleRepository repo;

    Module module;
    List<Module> modules;

    @BeforeEach
    void init() {
        Instant deadline = new Date(System.currentTimeMillis()).toInstant();
        module = Module.builder()
                .id(01)
                .name("Programmier Praktikum")
                .shortName("ProPra")
                .profSerial("Jens")
                .sevenHourLimit("2")
                .nineHourLimit("3")
                .seventeenHourLimit("2")
                .deadline(deadline)
                .build();

        entityManager.persist(module);

        modules = new ArrayList<>();
        modules.add(module);
    }

    @Test
    void findAll() {

    }

    @Test
    void findDistinctByName() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteAll() {
    }
}