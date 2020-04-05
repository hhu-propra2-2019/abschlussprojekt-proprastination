package mops.services.webServices;

import mops.services.dbServices.ModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import static org.mockito.Mockito.*;

class WebModuleServiceTest {

    private ModuleService moduleServiceMock = mock(ModuleService.class);
    private WebModuleService webModuleService = new WebModuleService(moduleServiceMock);

    @Test
    void generateErrorIfApplicantDeadlineAfterOrgaDeadline() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-02";
        String applicantTime = "00:00";
        String orgaDate = "2020-01-01";
        String orgaTime = "00:00";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(1)).addError(new FieldError("name",
                "applicantDeadlineDate",
                "Die Bearbeitungsfrist darf nicht vor der Bewerbungsfrist sein."));
    }

    @Test
    void generateErrorIfApplicantDeadlineAfterOrgaDeadline2() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-01";
        String applicantTime = "22:22";
        String orgaDate = "2020-01-01";
        String orgaTime = "11:11";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(1)).addError(new FieldError("name",
                "applicantDeadlineDate",
                "Die Bearbeitungsfrist darf nicht vor der Bewerbungsfrist sein."));
    }

    @Test
    void generateNoErrorIfDatesAndTimesAreFine() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-01";
        String applicantTime = "22:22";
        String orgaDate = "2020-01-02";
        String orgaTime = "22:22";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(0)).addError(any(ObjectError.class));
    }

    @Test
    void generateNoErrorIfDatesAndTimesAreFine2() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-01";
        String applicantTime = "22:22";
        String orgaDate = "2020-01-01";
        String orgaTime = "22:33";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(0)).addError(any(ObjectError.class));
    }

    @Test
    void generateErrorForNonsenseApplicantDate() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-13-02";
        String applicantTime = "00:00";
        String orgaDate = "2020-01-01";
        String orgaTime = "00:00";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(1)).addError(new FieldError("name",
                "applicantDeadlineDate",
                "Das Format der Bewerbungsfrist ist ungültig."));
    }

    @Test
    void generateErrorForNonsenseApplicantTime() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-01";
        String applicantTime = "25:00";
        String orgaDate = "2020-01-01";
        String orgaTime = "00:00";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(1)).addError(new FieldError("name",
                "applicantDeadlineDate",
                "Das Format der Bewerbungsfrist ist ungültig."));
    }

    @Test
    void generateErrorForNonsenseOrgaDate() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-01";
        String applicantTime = "00:00";
        String orgaDate = "2020-13-01";
        String orgaTime = "00:00";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(1)).addError(new FieldError("name",
                "orgaDeadlineDate",
                "Das Format der Bearbeitungsfrist ist ungültig."));
    }

    @Test
    void generateErrorForNonsenseOrgaTime() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        String applicantDate = "2020-01-01";
        String applicantTime = "00:00";
        String orgaDate = "2020-01-01";
        String orgaTime = "25:00";

        webModuleService.generateErrorIfApplicantDeadlineAfterOrgaDeadline(applicantDate, applicantTime,
                orgaDate, orgaTime, mockBindingResult, "name");

        verify(mockBindingResult, times(1)).addError(new FieldError("name",
                "orgaDeadlineDate",
                "Das Format der Bearbeitungsfrist ist ungültig."));
    }
}