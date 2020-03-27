package mops.services.dbServices;

import mops.model.Account;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.repositories.ApplicantRepository;
import mops.repositories.ApplicationRepository;
import mops.repositories.DistributionRepository;
import mops.repositories.EvaluationRepository;
import mops.repositories.ModuleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class DeletionServiceTest {

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    ApplicantRepository applicantRepository;

    @Mock
    ModuleRepository moduleRepository;

    @Mock
    Logger logger;

    @Mock
    DistributionRepository distributionRepository;

    @Mock
    EvaluationRepository evaluationRepository;

    @InjectMocks
    DeletionService deletionService;

    private Account account;

    @BeforeEach
    void setup() {
        Set<String> roles = new HashSet<>();
        account = new Account("Test", "Test", "Test", roles);
    }

    @Test
    void deleteApplicant() {
        Application application = Application.builder().id(1L).build();
        Applicant applicant = Applicant.builder()
                .id(1L)
                .uniserial("1")
                .application(application)
                .build();
        String expected = "Der Bewerber mit der Kennung: 1 wurde inkl. aller Bewerbungen gelöscht.";

        when(applicantRepository.findByUniserial(any(String.class))).thenReturn(applicant);

        String test = deletionService.deleteApplicant("1", account);

        assertThat(test).isEqualTo(expected);
        verify(applicantRepository, times(1)).deleteById(1L);
        verify(applicationRepository, times(1)).deleteById(1L);
        verify(applicantRepository, times(1)).findByUniserial("1");
    }

    @Test
    void deleteModule() {
        String expected = "Das Modul: 2 wurde inkl. aller Bewerbungen gelöscht.";
        Module module = Module.builder().id(2).name("2").build();

        when(moduleRepository.findDistinctByName(any(String.class))).thenReturn(module);

        String test = deletionService.deleteModule("2", account);

        assertThat(test).isEqualTo(expected);
        verify(moduleRepository, times(1)).deleteById(2L);
        verify(moduleRepository, times(1)).findDistinctByName("2");
    }

    @Test
    void deleteApplication() {
        String expected = "Die Bewerbung mit der ID: 3 wurde gelöscht.";

        String test = deletionService.deleteApplication(3, account);

        assertThat(test).isEqualTo(expected);
        verify(applicationRepository, times(1)).deleteById(3L);
    }

    @Test
    void deleteAll() {
        String expected = "Alle Daten wurden erfolgreich gelöscht";

        String test = deletionService.deleteAll(account);

        assertThat(test).isEqualTo(expected);
        verify(applicationRepository, times(0)).deleteAll();
        verify(applicantRepository, times(1)).deleteAll();
        verify(moduleRepository, times(1)).deleteAll();
        verify(distributionRepository, times(1)).deleteAll();
        verify(evaluationRepository, times(1)).deleteAll();
    }
}