package mops.services.logicServices;

import mops.model.classes.*;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebDistribution;
import mops.model.classes.webclasses.WebDistributorApplicant;
import mops.services.dbServices.*;
import mops.services.webServices.WebDistributionService;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void cleanDatabase() {
        applicantService.deleteAll();
        applicationService.deleteAll();
        evaluationService.deleteAll();
        dbDistributionService.deleteAll();
        moduleService.deleteAll();
    }

    private void fillDatabase() {
        Module ra = Module.builder()
                .name("RA")
                .sevenHourLimit("1")
                .nineHourLimit("2")
                .seventeenHourLimit("3")
                .build();
        Module aldat = Module.builder()
                .name("Aldat")
                .sevenHourLimit("3")
                .nineHourLimit("2")
                .seventeenHourLimit("1")
                .build();
        moduleService.save(ra);
        moduleService.save(aldat);

        List<Application> applications = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            if (i % 2 == 0) {
                if (i % 3 == 0) {
                    Application application = Application.builder()
                            .module(ra)
                            .priority(Priority.VERYHIGH)
                            .build();
                    applications.add(application);
                } else if (i % 3 == 1) {
                    Application application = Application.builder()
                            .module(ra)
                            .priority(Priority.HIGH)
                            .build();
                    applications.add(application);
                } else {
                    Application application = Application.builder()
                            .module(ra)
                            .priority(Priority.NEUTRAL)
                            .build();
                    applications.add(application);
                }
            } else {
                if (i % 3 == 0) {
                    Application application = Application.builder()
                            .module(aldat)
                            .priority(Priority.HIGH)
                            .build();
                    applications.add(application);
                } else if (i % 3 == 1) {
                    Application application = Application.builder()
                            .module(aldat)
                            .priority(Priority.NEUTRAL)
                            .build();
                    applications.add(application);
                } else {

                    Application application = Application.builder()
                            .module(aldat)
                            .priority(Priority.VERYHIGH)
                            .build();
                    applications.add(application);
                }
            }
        }

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

    @Test
    public void testDistribute() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        /*for (Applicant applicant : applicantService.findAll()) {
            System.out.println();
            System.out.println(applicant.getUniserial());
            for (Application application : applicant.getApplications()) {
                System.out.println("----- " + application.getModule().getName() + " " + application.getPriority().getLabel());
                System.out.println("----------- " + evaluationService.findByApplication(application).getPriority().getLabel() + " " + evaluationService.findByApplication(application).getHours());
            }
        }*/

        distributionService.distribute();

        /*List<Applicant> employees = dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees();

        for (Applicant applicant : employees) {
            System.out.println();
            System.out.println(applicant.getUniserial());
            for (Application application : applicant.getApplications()) {
                System.out.println("----- " + application.getModule().getName() + " " + application.getPriority().getLabel());
                System.out.println("----------- " + evaluationService.findByApplication(application).getPriority().getLabel() + " " + evaluationService.findByApplication(application).getHours());
            }
        }*/

        Distribution distribution1 = (Distribution.builder()
                .module(moduleService.findModuleByName("RA"))
                .employee(applicantService.findByUniserial("2"))
                .employee(applicantService.findByUniserial("3"))
                .employee(applicantService.findByUniserial("4"))
                .employee(applicantService.findByUniserial("8"))
                .employee(applicantService.findByUniserial("12"))
                .employee(applicantService.findByUniserial("14"))
                .build());

        Distribution distribution2 = (Distribution.builder()
                .module(moduleService.findModuleByName("Aldat"))
                .employee(applicantService.findByUniserial("5"))
                .employee(applicantService.findByUniserial("11"))
                .employee(applicantService.findByUniserial("10"))
                .employee(applicantService.findByUniserial("6"))
                .build());

        assertEquals(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().size(), distribution1.getEmployees().size());
        for (int i = 0; i < distribution1.getEmployees().size(); i++) {
            assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(distribution1.getEmployees().get(i)));
        }

        assertEquals(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().size(), distribution2.getEmployees().size());
        for (int i = 0; i < distribution2.getEmployees().size(); i++) {
            assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().contains(distribution2.getEmployees().get(i)));
        }
    }

    @Test
    public void testChangeAllFinalHours() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.changeAllFinalHours();

        List<Application> applications = applicationService.findAll();
        for (Application application : applications) {
            Evaluation evaluation = evaluationService.findByApplication(application);
            assertEquals(evaluation.getHours(), application.getFinalHours());
        }
    }

    @Test
    public void testFindAllUnassignedAfterDistribute() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        List<Applicant> actualApplicants = distributionService.findAllUnassigned();

        List<Applicant> expectedApplicants = new LinkedList<>();
        expectedApplicants.add(applicantService.findByUniserial("1"));
        expectedApplicants.add(applicantService.findByUniserial("7"));
        expectedApplicants.add(applicantService.findByUniserial("9"));
        expectedApplicants.add(applicantService.findByUniserial("13"));
        expectedApplicants.add(applicantService.findByUniserial("15"));

        assertEquals(actualApplicants.size(), expectedApplicants.size());
        for (int i = 0; i < expectedApplicants.size(); i++) {
            assertTrue(actualApplicants.contains(expectedApplicants.get(i)));
        }
    }

    @Test
    public void testFindAllUnassignedBeforeDistribute() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        List<Applicant> actualApplicants = distributionService.findAllUnassigned();

        assertEquals(actualApplicants, applicantService.findAll());
    }

    @Test
    public void testMoveApplicantFromModuleToModule() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("2").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getId()));

        assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().contains(applicantService.findByUniserial("2")));
        assertFalse(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicantService.findByUniserial("2")));
    }

    @Test
    public void testMoveApplicantFromUnassignedToModule() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("1").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getId()));

        assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("Aldat")).getEmployees().contains(applicantService.findByUniserial("1")));
        assertFalse(distributionService.findAllUnassigned().contains(applicantService.findByUniserial("1")));
    }

    @Test
    public void testMoveApplicantFromModuleToUnassigned() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("2").getId()), "-1");

        assertFalse(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicantService.findByUniserial("2")));
        assertTrue(distributionService.findAllUnassigned().contains(applicantService.findByUniserial("2")));
    }

    @Test
    public void testMoveApplicantToWrongModule() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        moduleService.save(Module.builder()
                .name("ProPra")
                .sevenHourLimit("1")
                .nineHourLimit("2")
                .seventeenHourLimit("3")
                .build());

        distributionService.distribute();
        distributionService.moveApplicant(Long.toString(applicantService.findByUniserial("2").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("ProPra")).getId()));

        assertFalse(dbDistributionService.findByModule(moduleService.findModuleByName("ProPra")).getEmployees().contains(applicantService.findByUniserial("2")));
        assertTrue(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees().contains(applicantService.findByUniserial("2")));
    }

    @Test
    public void testSort() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        WebDistributionService webDistributionService = new WebDistributionService(dbDistributionService, distributionService, evaluationService);

        List<WebDistribution> webDistributions = webDistributionService.convertDistributionsToWebDistributions();

        WebDistribution webDistribution = null;

        for (int i = 0; i < webDistributions.size(); i++) {
            if ("RA".equals(webDistributions.get(i).getModule())) {
                webDistribution = webDistributions.get(i);
            }
        }

        String[] actuaUniserial = new String[webDistribution.getWebDistributorApplicants().size()];
        for (int i = 0; i < actuaUniserial.length; i++) {
            actuaUniserial[i] = webDistribution.getWebDistributorApplicants().get(i).getUsername();
        }

        List<WebDistributorApplicant> sortedApplicants = webDistribution.getWebDistributorApplicants();

        String[] expectedUniserial = {"4", "12", "2", "8", "14", "3"};

        for (int i = 0; i < actuaUniserial.length; i++) {
            assertEquals(actuaUniserial[i], expectedUniserial[i]);
        }
    }

    @Test
    public void testGetTypeOfApplicant() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

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

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        distributionService.saveHours(Long.toString(applicantService.findByUniserial("2").getId()), Long.toString(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getId()), "4");

        int actualFinalHours = 0;

        for (Application application : applicantService.findByUniserial("2").getApplications()) {
            if ("RA".equals(application.getModule().getName())) {
                actualFinalHours = application.getFinalHours();
            }
        }

        assertEquals(actualFinalHours, 4);
    }

    @Test
    public void testSaveChecked() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.saveChecked(Long.toString(applicantService.findByUniserial("2").getId()), "true");

        assertTrue(applicantService.findByUniserial("2").isChecked());
    }

    @Test
    public void testGetSize() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        distributionService.distribute();

        assertEquals(distributionService.getSize(), 2);
    }

    @Test
    public void testGetSizeBeforeDistribute() {

        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

        fillDatabase();

        assertEquals(distributionService.getSize(), 0);
    }
}