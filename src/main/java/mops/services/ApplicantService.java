package mops.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mops.db.dto.ApplicantDTO;
import mops.db.repositories.ApplicantRepository;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Role;
import mops.model.classes.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicantService {

    @Autowired
    private ApplicantRepository repo;

    @SuppressWarnings("checkstyle:HiddenField")
    private void setRepo(final ApplicantRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns an application from given the parameters.
     * Seems unnecessary tho!
     *
     * @param uniquename Username
     * @param module   Module as String.
     * @param lecturer Lecturer as String.
     * @param semester Semester as String.
     * @param comment  Comment as String.
     * @param hours    Hours as Integer.
     * @param grade    Grade as Double.
     * @param role     Role as mops.classes.Role.
     * @return Application.
     */
    @SuppressWarnings({"checkstyle:ParameterNumber"})
    public Application createApplication(final String uniquename,
                                         final String module,
                                         final String lecturer,
                                         final String semester,
                                         final String comment,
                        //                 final int priority,
                                         final int hours,
                                         final double grade,
                                         final Role role) {
        return Application.builder()
                .applicantusername(uniquename)
                .module(module)
      //          .priority(priority)
                .comment(comment)
                .hours(hours)
                .grade(grade)
                .lecturer(lecturer)
                .semester(semester)
                .role(role)
                .build();
    }

    /**
    * Returns an Applicant given the input parameters.
     * Seems unnecessary!
     *
     * @param name         String name.
     * @param birthplace   String birthplace.
     * @param address      Address address.
     * @param birthday     String birthday.
     * @param nationality  String nationality.
     * @param course       String course.
     * @param status       String status.
     * @param certs        Certificate cert.
     * @param applications List<Application>
     * @return Applicant.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public Applicant createApplicant(final String name,
                                     final String birthplace,
                                     final Address address,
                                     final String birthday,
                                     final String nationality,
                                     final String course,
                                     final Status status,
                                     final Certificate certs,
                                     final List<Application> applications) {
        return Applicant.builder()
                .surname(name)
                .birthplace(birthplace)
                .address(address)
                .birthday(birthday)
                .nationality(nationality)
                .course(course)
                .status(status)
                .certs(certs)
                .applications(applications)
                .build();
    }


    /**
     * Saves Applicant.
     *
     * @param applicant Applicant.
     * @param username  Keycloak-name/Uni-Kennung.
     */
    public void save(final Applicant applicant, final String username) {
        String jsonString = objectToJsonString(applicant);
        ApplicantDTO dto = new ApplicantDTO();
        dto.setUsername(username);
        dto.setDetails(jsonString);

        Optional<Integer> opt = repo.getIdByUsername(username);
        opt.ifPresent(dto::setId);

        try {
            repo.save(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves through DTO into Database
     * @param dto The DTO used
     */

    public void save(final ApplicantDTO dto) {
        repo.save(dto);
    }

    /**
     * Returns Applicant or null given the username;
     *
     * @param username username sould be equal to Keycloak-Username.
     * @return Applicant.
     */
    public Applicant findByUsername(final String username) {
        ApplicantDTO dto = repo.findDistinctByUsername(username);
        Applicant applicant;
        applicant = dtoToModel(dto);
        return applicant;
    }

    /**
     * Find ApplicantDTO with username in Database
     * @param username
     * @return ApplicantDTO
     */
    public ApplicantDTO find(final String username) {
        return repo.findDistinctByUsername(username);
    }

    /**
     * Returns allApplications as List<Application>
     * @return List
     * Returns all Applications as a list.
     */
    public List<Application> getAllApplications() {
        ObjectMapper mapper = new ObjectMapper();
        List<Application> result = new ArrayList<>();
        for (String s : repo.findAllApplications()) {
            try {
                result.add(mapper.readValue(s, Application.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Returns all Applications given a module as a List.
     *
     * @param module String module name.
     * @return List<Application>
     */
    public List<Application> getAllApplicationsByModule(final String module) {
        ObjectMapper mapper = new ObjectMapper();
        List<Application> result = new ArrayList<>();
        for (String s : repo.findAllApplicationsByModuleName(module)) {
            try {
                result.add(mapper.readValue(s, Application.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Returns all Applicants as a List,
     * @return List<Applicant>
     */
    public List<Applicant> getAll() {
        List<Applicant> applicants = new ArrayList<>();
        repo.findAll().forEach(applicantDTO -> applicants.add(dtoToModel(applicantDTO)));
        return applicants;
    }

    /**
     * Parses Object to JsonString
     * @param object Object to parse
     * @return String
     */
    private String objectToJsonString(final Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String output = null;
        try {
            output = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return output;

    }
    /**
    * Parses DTO to Model
    * @param dto Applicantdto
    * @return the applicant
    */
    private Applicant dtoToModel(final ApplicantDTO dto) {
        if (dto == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        Applicant applicant = null;
        try {
            applicant = mapper.readValue(dto.getDetails(), Applicant.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return applicant;
    }

    /**
     * overrides applicant data without the applications
     *
     * @param applicant
     * @param username
     * @return updated applicant data
     */

    public Applicant overrideApplicantWithoutApplications(final Applicant applicant, final String username) {
        Applicant.ApplicantBuilder applicantBuilder = applicant.toBuilder();
        Applicant applicant1 = applicantBuilder
                .birthday(applicant.getBirthplace())
                .address(applicant.getAddress())
                .birthday(applicant.getBirthday())
                .nationality(applicant.getNationality())
                .course(applicant.getCourse())
                .status(applicant.getStatus())
                .certs(applicant.getCerts())
                .build();
        return applicant1;
    }
}
