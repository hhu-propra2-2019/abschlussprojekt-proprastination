package mops.services.webServices;

import mops.model.classes.*;
import mops.model.classes.Module;
import mops.model.classes.orgaWebClasses.OrgaApplication;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.EvaluationService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgaServiceTest {

    private ApplicantService mockApplicantService = mock(ApplicantService.class);
    private ApplicationService mockApplicationService = mock(ApplicationService.class);
    private EvaluationService mockEvaluationService = mock(EvaluationService.class);
    private OrgaService orgaService = new OrgaService(mockApplicantService,
            mockApplicationService, mockEvaluationService);

    private Module moduleMock1 = mock(Module.class);
    private Module moduleMock2 = mock(Module.class);
    private Applicant applicantMock1 = mock(Applicant.class);
    private Applicant applicantMock2 = mock(Applicant.class);

    private Application application1 = Application.builder()
            .id(42)
            .semester("SS20")
            .role(Role.PROOFREADER)
            .priority(Priority.VERYHIGH)
            .module(moduleMock1)
            .minHours(7)
            .maxHours(17)
            .finalHours(17)
            .lecturer("me111")
            .grade(3.7)
            .comment("Keine Panik auf der Titanic!")
            .build();

    private OrgaApplication orgaApplication1 = OrgaApplication.builder()
            .id(42)
            .applicant(applicantMock1)
            .semester("SS20")
            .role(Role.PROOFREADER)
            .priority(Priority.VERYHIGH)
            .module(moduleMock1)
            .minHours(7)
            .maxHours(17)
            .finalHours(17)
            .lecturer("me111")
            .grade(3.7)
            .comment("Keine Panik auf der Titanic!")
            .build();

    private Application application2 = Application.builder()
            .id(33)
            .semester("SS10")
            .role(Role.TUTOR)
            .priority(Priority.NEUTRAL)
            .module(moduleMock2)
            .minHours(9)
            .maxHours(9)
            .finalHours(9)
            .lecturer("you222")
            .grade(2.3)
            .comment("Mit dem 2. sieht man besser!")
            .build();

    private OrgaApplication orgaApplication2 = OrgaApplication.builder()
            .id(33)
            .applicant(applicantMock2)
            .semester("SS10")
            .role(Role.TUTOR)
            .priority(Priority.NEUTRAL)
            .module(moduleMock2)
            .minHours(9)
            .maxHours(9)
            .finalHours(9)
            .lecturer("you222")
            .grade(2.3)
            .comment("Mit dem 2. sieht man besser!")
            .build();

    @Test
    void getApplication() {
        when(mockApplicationService.findById(42)).thenReturn(application1);
        when(mockApplicantService.findByApplications(application1)).thenReturn(applicantMock1);

        OrgaApplication result = orgaService.getApplication("42");

        assertEquals(orgaApplication1, result);
    }

    @Test
    void getAllApplications() {
        when(mockApplicationService.findById(42)).thenReturn(application1);
        when(mockApplicantService.findByApplications(application1)).thenReturn(applicantMock1);
        when(mockApplicationService.findById(33)).thenReturn(application2);
        when(mockApplicantService.findByApplications(application2)).thenReturn(applicantMock2);

        List<Application> applications = new ArrayList<>();
        applications.add(application1);
        applications.add(application2);
        when(mockApplicationService.findAllByModuleId(1)).thenReturn(applications);

        List<OrgaApplication> expected = new ArrayList<>();
        expected.add(orgaApplication1);
        expected.add(orgaApplication2);

        List<OrgaApplication> result = orgaService.getAllApplications("1");

        assertEquals(expected, result);
    }
}