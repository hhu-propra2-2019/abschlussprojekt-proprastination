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

import static org.assertj.core.api.Assertions.assertThat;

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
        List<Module> list = repo.findAll();

        assertThat(list).isEqualTo(modules);
    }

    @Test
    void findDistinctByName() {
        Module m = repo.findDistinctByName(module.getName());

        assertThat(m).isEqualTo(module);

    }

    @Test
    void deleteById() {
        repo.deleteById(module.getId());

        assertThat(repo.count()).isEqualTo(0L);
    }

    @Test
    void deleteAll() {
        repo.deleteAll();

        assertThat(repo.count()).isEqualTo(0L);
    }
}