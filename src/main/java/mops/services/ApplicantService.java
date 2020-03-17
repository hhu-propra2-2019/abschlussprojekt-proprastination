package mops.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mops.db.dto.AddressDTO;
import mops.db.dto.ApplicantDTO;
import mops.db.dto.ApplicationDTO;
import mops.db.repositories.ApplicantRepository;
import mops.db.repositories.ApplicationRepository;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@EnableAutoConfiguration
public class ApplicantService {

    private ApplicantRepository applicantRepository;

    private ApplicationRepository applicationRepository;


    @Autowired
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:HiddenField"})
    public void setApplicationRepository(final ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Autowired
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:HiddenField"})
    public void setApplicantRepository(final ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    /**
     * Saves Applicant.
     *
     * @param applicant Applicant.
     */
    public void save(final Applicant applicant) {
        applicantRepository.save(applicantModelToDTO(applicant));
    }

    /**
     * Returns Applicant or null given the username;
     *
     * @param username username sould be equal to Keycloak-Username.
     * @return Applicant.
     */
    /*
    public Applicant findByUsername(final String username) {
        ApplicantDTO dto = repo.findDistinctByUsername(username);
        Applicant applicant;
        applicant = dtoToModel(dto);
        return applicant;
    }*/

    /**
     * Returns all Applications as a list.
     *
     * @return List<Application>
     */
    public List<Application> getAllApplications() {
        List<Application> all = new ArrayList<>();
        var iter = applicationRepository.findAll();
        iter.forEach(application -> all.add(applicationDTOtoModel(application)));

        return all;
    }

    public List<Applicant> getAllApplicants() {
        List<Applicant> all = new ArrayList<>();
        var iter = applicantRepository.findAll();
        iter.forEach(applicant -> all.add(applicantDTOToModel(applicant)));
        return all;
    }

    /**
     * Returns all Applications given a module as a List.
     *
     * @param module String module name.
     * @return List<Application>
     */
    public List<Application> getAllApplicationsByModule(final String module) {
        List<Application> result = new ArrayList<>();
        var iter = applicationRepository.findAllByModule(module);
        iter.forEach(application -> result.add(applicationDTOtoModel(application)));
        return result;
    }


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

    private Applicant applicantDTOToModel(final ApplicantDTO dto) {

        Set<ApplicationDTO> applications = dto.getApplications();
        Set<Application> newapplications = new HashSet<>();

        applications.iterator().forEachRemaining(application -> {
            Application app = Application.builder()
                    .priority(application.getPriority())
                    .id(dto.getId())
                    .semester(application.getSemester())
                    .role(application.getRole())
                    .lecturer(application.getLecturer())
                    .grade(application.getGrade())
                    .hours(application.getHours())
                    .module(application.getModule())
                    .build();
            newapplications.add(app);
        });

        Applicant applicant = Applicant.builder()
                .name(dto.getName())
                .birthplace(dto.getBirthplace())
                .birthday(dto.getBirthday())
                .comment(dto.getComment())
                .status(dto.getStatus())
                .surename(dto.getSurname())
                .nationality(dto.getNationality())
                .uniserial(dto.getUniserial())
                .address(Address.builder()
                        .city(dto.getAddress().getCity())
                        .country(dto.getAddress().getCountry())
                        .street(dto.getAddress().getStreet())
                        .zipcode(dto.getAddress().getZipcode())
                        .build())
                .certs(Certificate.builder()
                        .name(dto.getCertificate().getName())
                        .course(dto.getCertificate().getCourse())
                        .build())
                .applications(newapplications)
                .build();

        return applicant;


    }

    private Application applicationDTOtoModel(final ApplicationDTO dto) {
        return Application.builder()
                .priority(dto.getPriority())
                .id(dto.getId())
                .semester(dto.getSemester())
                .role(dto.getRole())
                .lecturer(dto.getLecturer())
                .grade(dto.getGrade())
                .hours(dto.getHours())
                .module(dto.getModule())
                .build();
    }

    private ApplicationDTO applicationModelToDTO(final Application model) {
        return new ApplicationDTO(model.getId(),
                model.getHours(),
                model.getModule(),
                model.getPriority(),
                model.getGrade(),
                model.getLecturer(),
                model.getSemester(),
                model.getRole());
    }

    private ApplicantDTO applicantModelToDTO(final Applicant applicant) {
        Optional<Long> id = applicantRepository.getIdByUsername(applicant.getUniserial());

        Address oldAddress = applicant.getAddress();
        AddressDTO address = new AddressDTO(oldAddress.getStreet(), oldAddress.getCity(),
                oldAddress.getCountry(), oldAddress.getZipcode());

        Set<ApplicationDTO> applicationDTOS = new HashSet<>();
        applicant.getApplications().forEach(application -> applicationDTOS.add(applicationModelToDTO(application)));


        ApplicantDTO app = new ApplicantDTO();
        app.setBirthday(applicant.getBirthday());
        app.setComment(applicant.getComment());
        id.ifPresent(app::setId);
        app.setCourse(applicant.getCourse());
        app.setBirthplace(applicant.getBirthplace());
        app.setName(applicant.getName());
        app.setSurname(applicant.getSurename());
        app.setNationality(applicant.getNationality());
        app.setStatus(applicant.getStatus());
        app.setAddress(address);
        app.setApplications(applicationDTOS);

        return app;

    }


}
