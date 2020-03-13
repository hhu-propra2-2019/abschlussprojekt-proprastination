package mops.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mops.db.dto.ApplicantDTO;
import mops.db.repositories.ApplicantRepository;
import mops.model.classes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
     *
     * @param username Username
     * @param module   Module as String.
     * @param lecturer Lecturer as String.
     * @param semester Semester as String.
     * @param comment  Comment as String.
     * @param hours    Hours as Integer.
     * @param grade    Grade as Double.
     * @param role     Role as mops.classes.Role.
     * @return Application.
     */
    public Application createApplication(final String username,
                                         final String module,
                                         final String lecturer,
                                         final String semester,
                                         final String comment,
                                         final int hours,
                                         final double grade,
                                         final Role role) {
        Application application = Application.builder()
                .applicantusername(username)
                .module(module)
                .comment(comment)
                .hours(hours)
                .grade(grade)
                .lecturer(lecturer)
                .semester(semester)
                .role(role)
                .build();
        return application;
    }

    /**
     * Create Applicant with Builder
     * @param name
     * @param birthplace
     * @param address
     * @param birthday
     * @param nationality
     * @param course
     * @param status
     * @param certs
     * @param applications
     * @return Applicant created
     */
    public Applicant createApplicant(final String name,
                                     final String birthplace,
                                     final Address address,
                                     final String birthday,
                                     final String nationality,
                                     final String course,
                                     final Status status,
                                     final Certificate certs,
                                     final List<Application> applications) {
        Applicant applicant = Applicant.builder()
                .name(name)
                .birthplace(birthplace)
                .address(address)
                .birthday(birthday)
                .nationality(nationality)
                .course(course)
                .status(status)
                .certs(certs)
                .applications(applications)
                .build();
        return applicant;
    }


    /**
     * Saves Applicant.
     *
     * @param applicant
     * @param username
     */
    public void save(final Applicant applicant, final String username) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(applicant);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ApplicantDTO dto = new ApplicantDTO();
        dto.setUsername(username);
        dto.setDetails(jsonString);

        Optional<Integer> opt = repo.getIdByUsername(username);
        opt.ifPresent(value -> dto.setId(value));

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
        Applicant applicant = null;
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
     */
    public List<Application> getAllApplications() {
        ObjectMapper mapper = new ObjectMapper();
        List<String> result = repo.findAllApplications();
        List<Application> applications = new ArrayList<>();
        List<Application> temp = new ArrayList<>();

        for (String s : result) {
            try {
                Application[] array = mapper.readValue(s, Application[].class);
                temp = Arrays.asList(array);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            applications.addAll(temp);
        }

        return applications;
    }


    /**
     * Returns an Iterable over the ApplicantDTOs.
     *
     * @return Iterable<ApplicantDTO>
     */
    @Deprecated
    public Iterable<ApplicantDTO> getAllIterable() {
        return repo.findAll();
    }

    /**
     * Returns all Applicants as a List,
     * @return List<Applicant>
     */
    public List<Applicant> getAll() {
        List<Applicant> applicants = new ArrayList<>();
        repo.findAll().forEach(applicantDTO -> {
            applicants.add(dtoToModel(applicantDTO));
        });
        return applicants;
    }

    /**
     * Parses Object to JsonString
     * @param object Object to parse
     * @return String
     */
    private String objectToJsonString(final Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String output = "";
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


}
