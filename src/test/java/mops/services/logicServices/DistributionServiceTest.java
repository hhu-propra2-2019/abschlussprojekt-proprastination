package mops.services.logicServices;

import com.github.javafaker.App;
import mops.model.classes.*;
import mops.model.classes.Module;
import mops.services.dbServices.*;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void testDistribute() {
        DistributionService distributionService = new DistributionService(dbDistributionService, moduleService, applicantService, applicationService, evaluationService);

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
    /*
        for (Applicant applicant : applicantService.findAll()) {
            System.out.println();
            System.out.println(applicant.getUniserial());
            for (Application application : applicant.getApplications()) {
                System.out.println("----- " + application.getModule().getName() + " " + application.getPriority().getLabel());
                System.out.println("----------- " + evaluationService.findByApplication(application).getPriority().getLabel() + " " + evaluationService.findByApplication(application).getHours());
            }
        }
     */

        distributionService.distribute();

        //List<Distribution> distributions = new LinkedList<>();
        Distribution distribution = (Distribution.builder()
                .module(moduleService.findModuleByName("RA"))
                .employee(applicantService.findByUniserial("1"))
                .employee(applicantService.findByUniserial("12"))
                .employee(applicantService.findByUniserial("3"))
                .employee(applicantService.findByUniserial("2"))
                .employee(applicantService.findByUniserial("8"))
                .employee(applicantService.findByUniserial("14"))
                .build());
        /*
        distributions.add(Distribution.builder()
                .module(moduleService.findModuleByName("Aldat"))
                .employee(applicantService.findByUniserial("5"))
                .employee(applicantService.findByUniserial("11"))
                .employee(applicantService.findByUniserial("4"))
                .employee(applicantService.findByUniserial("10"))
                .employee(applicantService.findByUniserial("6"))
                .build());*/

        assert(dbDistributionService.findByModule(moduleService.findModuleByName("RA")).getEmployees(),
                containsInAnyOrder(distribution.getEmployees()));
    }


}