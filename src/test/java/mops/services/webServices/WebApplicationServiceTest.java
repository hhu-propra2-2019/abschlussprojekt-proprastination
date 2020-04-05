package mops.services.webServices;

import mops.model.Account;
import mops.model.classes.*;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.model.classes.webclasses.WebCertificate;
import mops.services.CSVService;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.ModuleService;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebApplicationServiceTest {

    private ApplicantService applicantServiceMock = mock(ApplicantService.class);
    private StudentService studentServiceMock = mock(StudentService.class);
    private ModuleService moduleServiceMock = mock(ModuleService.class);
    private ApplicationService applicationServiceMock = mock(ApplicationService.class);

    private WebApplicationService webApplicationService = new WebApplicationService(
            applicantServiceMock, studentServiceMock, moduleServiceMock, applicationServiceMock);

    @Test
    void createNewApplicantIfNoneWasFoundAndReturnFalse() {
        Account account = new Account("keinPlan777", "kein@plan.de", null, new HashSet<>());
        when(applicantServiceMock.findByUniserial("keinPlan777")).thenReturn(null);
        List<Module> moduleList = new ArrayList<>();
        when(moduleServiceMock.getModules()).thenReturn(moduleList);
        when(studentServiceMock.getAllNotfilledModules(null, moduleList)).thenReturn(new ArrayList<>());
        Model modelMock = mock(Model.class);

        boolean returnValue = webApplicationService.createNewApplicantIfNoneWasFound(account, modelMock);

        verify(modelMock, times(1)).addAttribute("account", account);
        verify(modelMock, times(1)).addAttribute("countries", CSVService.getCountries());
        verify(modelMock, times(1)).addAttribute("courses", CSVService.getCourses());
        verify(modelMock, times(1)).addAttribute("webApplicant", new WebApplicant());
        verify(modelMock, times(1)).addAttribute("webAddress", new WebAddress());
        verify(modelMock, times(1)).addAttribute("webCertificate", new WebCertificate());
        verify(modelMock, times(1)).addAttribute("modules", new ArrayList<Module>());
        assertFalse(returnValue);
    }

    @Test
    void createNoApplicantIfApplicantWasFoundAndReturnTrue() {
        Account account = new Account("keinPlan777", "kein@plan.de", null, new HashSet<>());

        WebAddress webAddress = WebAddress.builder()
                .zipcode("12345")
                .number("42")
                .country("Musterland")
                .city("Atlantis")
                .street("Winkelgasse")
                .build();
        WebCertificate webCerts = WebCertificate.builder()
                .graduationcourse("Angeln")
                .graduation("Diplom")
                .build();
        WebApplicant webApplicant = WebApplicant.builder()
                .birthday("2000-12-12")
                .birthplace("Irgendwo")
                .comment("Hi!")
                .course("Rudern")
                .gender("female")
                .nationality("Mustervania")
                .status("Änderung")
                .build();

        Application applicationMock = mock(Application.class);
        Address addressMock = mock(Address.class);
        Certificate certificateMock = mock(Certificate.class);
        Applicant applicant = Applicant.builder()
                .application(applicationMock)
                .address(addressMock)
                .certs(certificateMock)
                .build();

        when(applicantServiceMock.findByUniserial("keinPlan777")).thenReturn(applicant);
        List<Module> moduleList1 = new ArrayList<>();
        moduleList1.add(mock(Module.class));
        moduleList1.add(mock(Module.class));
        List<Module> moduleList2 = new ArrayList<>();
        moduleList2.add(mock(Module.class));
        when(moduleServiceMock.getModules()).thenReturn(moduleList1);
        when(studentServiceMock.getAllNotfilledModules(applicant, moduleList1)).thenReturn(moduleList2);
        when(studentServiceMock.getExistingApplicant(applicant)).thenReturn(webApplicant);
        when(studentServiceMock.getExistingAddress(addressMock)).thenReturn(webAddress);
        when(studentServiceMock.getExistingCertificate(certificateMock)).thenReturn(webCerts);
        Model modelMock = mock(Model.class);

        boolean returnValue = webApplicationService.createNewApplicantIfNoneWasFound(account, modelMock);

        verify(modelMock, times(1)).addAttribute("account", account);
        verify(modelMock, times(1)).addAttribute("countries", CSVService.getCountries());
        verify(modelMock, times(1)).addAttribute("courses", CSVService.getCourses());
        verify(modelMock, times(1)).addAttribute("webApplicant", webApplicant);
        verify(modelMock, times(1)).addAttribute("webAddress", webAddress);
        verify(modelMock, times(1)).addAttribute("webCertificate", webCerts);
        verify(modelMock, times(1)).addAttribute("modules", moduleList2);
        assertTrue(returnValue);
    }

    @Test
    void getApplicantUniserialsByModule() {
        Module moduleMock = mock(Module.class);

        Applicant applicantMock1 = mock(Applicant.class);
        Applicant applicantMock2 = mock(Applicant.class);
        List<Applicant> expected = new ArrayList<>();
        expected.add(applicantMock1);
        expected.add(applicantMock2);

        List<Application> applications = new ArrayList<>();
        Application application1 = mock(Application.class);
        Application application2 = mock(Application.class);
        applications.add(application1);
        applications.add(application2);

        when(moduleServiceMock.findModuleByName("testModule")).thenReturn(moduleMock);
        when(applicationServiceMock.findApplicationsByModule(moduleMock)).thenReturn(applications);
        when(applicantServiceMock.findByApplications(application1)).thenReturn(applicantMock1);
        when(applicantServiceMock.findByApplications(application2)).thenReturn(applicantMock2);

        List<Applicant> result = webApplicationService.getApplicantUniserialsByModule("testModule");

        assertEquals(expected, result);
    }

    @Test
    void removeCurrentModuleFromListAndSavePersonalInfo() {
        KeycloakAuthenticationToken tokenMock = mock(KeycloakAuthenticationToken.class);
        KeycloakPrincipal principalMock = mock(KeycloakPrincipal.class);
        when(tokenMock.getPrincipal()).thenReturn(principalMock);
        when(principalMock.getName()).thenReturn("Muster Name");
        KeycloakSecurityContext contextMock = mock(KeycloakSecurityContext.class);
        when(principalMock.getKeycloakSecurityContext()).thenReturn(contextMock);
        IDToken idTokenMock = mock(IDToken.class);
        when(contextMock.getIdToken()).thenReturn(idTokenMock);
        when(idTokenMock.getEmail()).thenReturn("muster@mail.net");
        OidcKeycloakAccount keycloakAccountMock = mock(OidcKeycloakAccount.class);
        when(tokenMock.getAccount()).thenReturn(keycloakAccountMock);
        Set<String> roles = new HashSet<>();
        when((keycloakAccountMock.getRoles())).thenReturn(roles);

        WebApplicant webApplicantMock = mock(WebApplicant.class);
        WebAddress webAddressMock = mock(WebAddress.class);
        Model modelMock = mock(Model.class);
        WebCertificate webCertificateMock = mock(WebCertificate.class);
        Module moduleMock1 = mock(Module.class);
        Module moduleMock2 = mock(Module.class);
        Applicant applicantMock = mock(Applicant.class);

        List<Module> fullList = new ArrayList<>();
        fullList.add(moduleMock1);
        fullList.add(moduleMock2);

        List<Module> expectedList = new ArrayList<>();
        expectedList.add(moduleMock2);

        when(studentServiceMock.savePersonalData(tokenMock, webApplicantMock, webAddressMock, webCertificateMock)).thenReturn(applicantMock);
        when(moduleServiceMock.getModules()).thenReturn(fullList);
        when(moduleServiceMock.findModuleByName("Tieftauchen")).thenReturn(moduleMock1);
        when(studentServiceMock.getAllNotfilledModules(applicantMock, fullList)).thenReturn(fullList);

        webApplicationService.removeCurrentModuleFromListAndSavePersonalInfo(tokenMock,
                webApplicantMock, webAddressMock, modelMock, "Tieftauchen", webCertificateMock);

        verify(studentServiceMock, times(1)).getAllNotfilledModules(applicantMock, fullList);
        verify(studentServiceMock, times(1)).savePersonalData(tokenMock,
                webApplicantMock, webAddressMock, webCertificateMock);
        verify(modelMock, times(1)).addAttribute(eq("account"),
               any(Account.class));
        verify(modelMock, times(1)).addAttribute("newModule", moduleMock1);
        verify(modelMock, times(1)).addAttribute("semesters", CSVService.getSemester());
        verify(modelMock, times(1)).addAttribute("modules", expectedList);
        verify(modelMock, times(1)).addAttribute("webApplication",
                WebApplication.builder().module("Tieftauchen").build());
    }

}