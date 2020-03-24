package mops.services;

import mops.model.classes.Application;
import mops.model.classes.orgaWebClasses.OrgaApplication;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgaService {

    private final ApplicantService applicantService;
    private final ApplicationService applicationService;

    /**
     * Lets Spring inject the services
     * @param applicantService
     * @param applicationService
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrgaService(final ApplicantService applicantService, final ApplicationService applicationService) {
        this.applicantService = applicantService;
        this.applicationService = applicationService;
    }

    private OrgaApplication wrapApplication(final Application application) {
        return OrgaApplication.builder()
                .id(application.getId())
                .minHours(application.getMinHours())
                .finalHours(application.getFinalHours())
                .maxHours(application.getMaxHours())
                .priority(application.getPriority())
                .grade(application.getGrade())
                .module(application.getModule())
                .lecturer(application.getLecturer())
                .semester(application.getSemester())
                .role(application.getRole())
                .comment(application.getComment())
                .applicant(applicantService.findByApplications(application))
                .build();
    }

    private List<OrgaApplication> wrapApplications(final List<Application> applications) {
        return applications.stream().map(this::wrapApplication).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Finds all applications from a module
     * @param id the modules ID
     * @return list of applications
     */
    public List<OrgaApplication> getAllApplications(final String id) {
        return wrapApplications(applicationService.findAllByModuleId(Long.parseLong(id)));
    }

    /**
     * Gets a single applications
     * @param id the applications ID
     * @return list of applications
     */
    public OrgaApplication getApplication(final String id) {
        return wrapApplication(applicationService.findById(Long.parseLong(id)));
    }
}
