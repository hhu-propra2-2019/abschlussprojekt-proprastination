package mops.services.webServices;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Evaluation;
import mops.model.classes.Priority;
import mops.model.classes.orgaWebClasses.WebList;
import mops.model.classes.orgaWebClasses.OrgaApplication;
import mops.model.classes.orgaWebClasses.WebListClass;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.EvaluationService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrgaService {

    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private static final Priority BASEPRIORITY = Priority.NEUTRAL;
    private static final int BASEHOURS = 9;
    private final EvaluationService evaluationService;


    /**
     * Lets Spring inject the services
     *
     * @param applicantService   applicantservice
     * @param applicationService applicationservice
     * @param evaluationService  evalservice
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public OrgaService(final ApplicantService applicantService, final ApplicationService applicationService,
                       final EvaluationService evaluationService) {
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.evaluationService = evaluationService;
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

    private WebList wrapListObject(final Application application) {
        Optional<Evaluation> evaluation = Optional.ofNullable(evaluationService.findByApplication(application));
        Applicant applicant = applicantService.findByApplications(application);
        WebList webList = WebList.builder()
                .firstName(applicant.getFirstName())
                .surname(applicant.getSurname())
                .uniserial(applicant.getUniserial())
                .grade(application.getGrade())
                .id(application.getId())
                .minHours(application.getMinHours())
                .maxHours(application.getMaxHours())
                .studentPriority(application.getPriority())
                .role(application.getRole())
                .priority(BASEPRIORITY.getValue())
                .finalHours(BASEHOURS)
                .build();
        evaluation.ifPresent(eval -> {
            webList.setPriority(eval.getPriority().getValue());
            webList.setFinalHours(eval.getHours());
        });
        return webList;
    }

    private List<WebList> wrapListObjects(final List<Application> applications) {
        return applications.stream().map(this::wrapListObject).collect(Collectors.toList());
    }

    /**
     * Returns All applications in the fitting format.
     *
     * @param id module id.
     * @return Lst of WebList applications.
     */
    public List<WebList> getAllListEntries(final String id) {
        return wrapListObjects(applicationService.findAllByModuleId(Long.parseLong(id)));
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
     *
     * @param id the applications ID
     * @return list of applications
     */
    public OrgaApplication getApplication(final String id) {
        return wrapApplication(applicationService.findById(Long.parseLong(id)));
    }

    /**
     * Saves a List of WebList applications.
     *
     * @param evaluations WebListClass applications.
     */
    public void saveEvaluations(final WebListClass evaluations) {
        evaluations.getApplications().forEach(eval -> {
            Application application = applicationService.findById(eval.getId());
            Evaluation evaluation = evaluationService.findByApplication(application);
            if (evaluation == null) {
                evaluation = Evaluation.builder()
                        .application(application)
                        .build();
            }
            Evaluation newEvaluation = evaluation.toBuilder()
                    .priority(Priority.get(eval.getPriority()))
                    .hours(eval.getFinalHours())
                    .build();
            evaluationService.save(newEvaluation);
        });
    }
}
