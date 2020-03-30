package mops.services.logicServices;

import mops.model.classes.*;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.services.dbServices.*;
import mops.services.webServices.WebDistributionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DistributionServiceTest {

    @Autowired
    ApplicantService applicantService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    EvaluationService evaluationService;
    @Autowired
    DbDistributionService dbDistributionService;
    @Autowired
    ModuleService moduleService;
    @Autowired
    DistributionService distributionService;

    @AfterEach
    public void cleanDatabase() {
        applicantService.deleteAll();
        applicationService.deleteAll();
        evaluationService.deleteAll();
        dbDistributionService.deleteAll();
        moduleService.deleteAll();
    }

    @BeforeEach
    public void fillDatabase() {
        createModules();
        List<Application> applications = createApplications();
        createApplicants(applications);
        createEvaluations(applications);
    }

    private void createModules() {
        moduleService.save(Module.builder()
                .name("RA")
                .sevenHourLimit("1")
                .nineHourLimit("2")
                .seventeenHourLimit("3")
                .deadlineDate("")
                .deadlineTime("")
                .build());
        moduleService.save(Module.builder()
                .name("Aldat")
                .sevenHourLimit("3")
                .nineHourLimit("2")
                .seventeenHourLimit("1")
                .deadlineDate("")
                .deadlineTime("")
                .build());
    }

    private void createEvaluations(List<Application> applications) {
        for (int i = 0; i < applications.size(); i++) {
            if(i % 3 == 0) {
                if ((i+1) % 4 == 0) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.NEUTRAL)
                            .hours(7)
                            .build());
                } else if ((i+1) % 4 == 1) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.HIGH)
                            .hours(7)
                            .build());
                } else if ((i-1) % 4 == 2) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.NEGATIVE)
                            .hours(7)
                            .build());
                } else {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.VERYHIGH)
                            .hours(7)
                            .build());
                }
            } else if (i % 3 == 1) {
                if ((i+1) % 4 == 0) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.VERYHIGH)
                            .hours(9)
                            .build());
                } else if ((i+1) % 4 == 1) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.NEUTRAL)
                            .hours(9)
                            .build());
                } else if ((i+1) % 4 == 2) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.NEGATIVE)
                            .hours(9)
                            .build());
                } else {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.HIGH)
                            .hours(9)
                            .build());
                }
            } else {
                if (i % 4 == 0) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.NEGATIVE)
                            .hours(17)
                            .build());
                } else if (i % 4 == 1) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.NEUTRAL)
                            .hours(17)
                            .build());
                } else if (i % 4 == 2) {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.HIGH)
                            .hours(17)
                            .build());
                } else {
                    evaluationService.save(Evaluation.builder()
                            .application(applications.get(i))
                            .priority(Priority.VERYHIGH)
                            .hours(17)
                            .build());
                }
            }
        }
    }

    private void createApplicants(List<Application> applications) {
        for (int i = 0; i < applications.size(); i++) {
            if(i % 2 == 1) {
                applicantService.saveApplicant(Applicant.builder()
                        .uniserial(Long.toString((i + 1)/2))
                        .application(applications.get(i - 1))
                        .application(applications.get(i))
                        .certs(Certificate.builder()
                                .name("Keins")
                                .build())
                        .checked(false)
                        .build());
            }
        }
    }

    private List<Application> createApplications() {
        List<Application> applications = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            if (i % 2 == 0) {
                if (i % 3 == 0) {
                    Application application = Application.builder()
                            .module(moduleService.findModuleByName("RA"))
                            .priority(Priority.VERYHIGH)
                            .build();
                    applications.add(application);
                } else if (i % 3 == 1) {
                    Application application = Application.builder()
                            .module(moduleService.findModuleByName("RA"))
                            .priority(Priority.HIGH)
                            .build();
                    applications.add(application);
                } else {
                    Application application = Application.builder()
                            .module(moduleService.findModuleByName("RA"))
                            .priority(Priority.NEUTRAL)
                            .build();
                    applications.add(application);
                }
            } else {
                if (i % 3 == 0) {
                    Application application = Application.builder()
                            .module(moduleService.findModuleByName("Aldat"))
                            .priority(Priority.HIGH)
                            .build();
                    applications.add(application);
                } else if (i % 3 == 1) {
                    Application application = Application.builder()
                            .module(moduleService.findModuleByName("Aldat"))
                            .priority(Priority.NEUTRAL)
                            .build();
                    applications.add(application);
                } else {

                    Application application = Application.builder()
                            .module(moduleService.findModuleByName("Aldat"))
                            .priority(Priority.VERYHIGH)
                            .build();
                    applications.add(application);
                }
            }
        }
        return applications;
    }

    @Test
    public void testDistribute() {
        distributionService.distribute();

        List<Applicant> applicantsRA = new LinkedList<>();
        applicantsRA.add(applicantService.findByUniserial("2"));
        applicantsRA.add(applicantService.findByUniserial("3"));
        applicantsRA.add(applicantService.findByUniserial("4"));
        applicantsRA.add(applicantService.findByUniserial("8"));
        applicantsRA.add(applicantService.findByUniserial("12"));
        applicantsRA.add(applicantService.findByUniserial("14"));

        List<Applicant> applicantsAldat = new LinkedList<>();
        applicantsAldat.add(applicantService.findByUniserial("5"));
        applicantsAldat.add(applicantService.findByUniserial("11"));
        applicantsAldat.add(applicantService.findByUniserial("10"));
        applicantsAldat.add(applicantService.findByUniserial("6"));

        assertEquals(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().size(), applicantsRA.size());
        for (Applicant applicant : applicantsRA) {
            assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicant));
        }

        assertEquals(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().size(), applicantsAldat.size());
        for (Applicant applicant : applicantsAldat) {
            assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().contains(applicant));
        }
    }

    @Test
    public void testChangeAllFinalHours() {
        distributionService.changeAllFinalHours();

        List<Application> applications = applicationService.findAll();
        for (Application application : applications) {
            Evaluation evaluation = evaluationService.findByApplication(application);
            assertEquals(evaluation.getHours(), application.getFinalHours());
        }
    }

    @Test
    public void testFindAllUnassignedAfterDistribute() {
        List<Applicant> expectedApplicants = new LinkedList<>();
        expectedApplicants.add(applicantService.findByUniserial("1"));
        expectedApplicants.add(applicantService.findByUniserial("7"));
        expectedApplicants.add(applicantService.findByUniserial("9"));
        expectedApplicants.add(applicantService.findByUniserial("13"));
        expectedApplicants.add(applicantService.findByUniserial("15"));

        distributionService.distribute();
        List<Applicant> actualApplicants = distributionService.findAllUnassigned();

        assertEquals(actualApplicants.size(), expectedApplicants.size());
        for (Applicant expectedApplicant : expectedApplicants) {
            assertTrue(actualApplicants.contains(expectedApplicant));
        }
    }

    @Test
    public void testFindAllUnassignedBeforeDistribute() {
        List<Applicant> actualApplicants = distributionService.findAllUnassigned();

        assertEquals(actualApplicants, applicantService.findAll());
    }

    @Test
    public void testMoveApplicantFromModuleToModule() {
        distributionService.distribute();
        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("2").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getId()));

        assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().contains(applicantService.findByUniserial("2")));
        assertFalse(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicantService.findByUniserial("2")));
    }

    @Test
    public void testMoveApplicantFromUnassignedToModule() {
        distributionService.distribute();
        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("1").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getId()));

        assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().contains(applicantService.findByUniserial("1")));
        assertFalse(distributionService.findAllUnassigned().contains(applicantService.findByUniserial("1")));
    }

    @Test
    public void testMoveApplicantFromModuleToUnassigned() {
        distributionService.distribute();
        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("2").getId()), "-1");

        assertFalse(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicantService.findByUniserial("2")));
        assertTrue(distributionService.findAllUnassigned().contains(applicantService.findByUniserial("2")));
    }

    @Test
    public void testMoveApplicantToWrongModule() {
        moduleService.save(Module.builder()
                .name("ProPra")
                .sevenHourLimit("1")
                .nineHourLimit("2")
                .seventeenHourLimit("3")
                .deadlineDate("")
                .deadlineTime("")
                .build());

        distributionService.distribute();
        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("2").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("ProPra")).getId()));

        assertFalse(dbDistributionService.findByModule(moduleService.findModuleByName("ProPra")).getEmployees().contains(applicantService.findByUniserial("2")));
        assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicantService.findByUniserial("2")));
    }

    @Test
    public void testSort() {
        String[] expectedUniserial = {"4", "12", "2", "8", "14", "3"};

        distributionService.distribute();
        WebDistributionService webDistributionService = new WebDistributionService(dbDistributionService, distributionService, evaluationService);
        List<WebDistribution> webDistributions = webDistributionService.convertDistributionsToWebDistributions();
        WebDistribution webDistribution = null;
        for (WebDistribution distribution : webDistributions) {
            if ("RA".equals(distribution.getModule())) {
                webDistribution = distribution;
            }
        }
        String[] actuaUniserial = new String[webDistribution.getWebDistributorApplicants().size()];
        for (int i = 0; i < actuaUniserial.length; i++) {
            actuaUniserial[i] = webDistribution.getWebDistributorApplicants().get(i).getUsername();
        }

        for (int i = 0; i < actuaUniserial.length; i++) {
            assertEquals(actuaUniserial[i], expectedUniserial[i]);
        }
    }

    @Test
    public void testGetTypeOfApplicant() {
        Applicant applicant1 = Applicant.builder()
                .certs(Certificate.builder()
                        .name("Keins")
                        .build())
                .uniserial("1")
                .build();

        Applicant applicant2 = Applicant.builder()
                .certs(Certificate.builder()
                        .name("Jodeldiplom")
                        .build())
                .uniserial("1")
                .build();

        assertEquals(distributionService.getTypeOfApplicant(applicant1), "SHK");
        assertEquals(distributionService.getTypeOfApplicant(applicant2), "WHB");
    }

    @Test
    public void testSaveHours() {
        int actualFinalHours = 0;

        distributionService.distribute();
        distributionService.saveHours(Long.toString(applicantService.findByUniserial("2").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getId()), "4");

        for (Application application : applicantService.findByUniserial("2").getApplications()) {
            if ("RA".equals(application.getModule().getName())) {
                actualFinalHours = application.getFinalHours();
            }
        }

        assertEquals(actualFinalHours, 4);
    }

    @Test
    public void testSaveChecked() {
        distributionService.saveChecked(Long.toString(applicantService.findByUniserial("2").getId()), "true");

        assertTrue(applicantService.findByUniserial("2").isChecked());
    }

    @Test
    public void testGetSize() {
        distributionService.distribute();

        assertEquals(distributionService.getSize(), 2);
    }

    @Test
    public void testGetSizeBeforeDistribute() {
        assertEquals(distributionService.getSize(), 0);
    }
}