package mops.services;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Applicant.ApplicantBuilder;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Module;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebApplication;
import mops.model.classes.webclasses.WebCertificate;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@SuppressWarnings("checkstyle:HiddenField")
public class StudentService {

    private ApplicantService applicantService;
    private ModuleService moduleService;


    /**
     * Setup the applicantserives
     *
     * @param applicantService applicant.
     * @param moduleService    modulesservice.
     */
    public StudentService(final ApplicantService applicantService, final ModuleService moduleService) {
        this.applicantService = applicantService;
        this.moduleService = moduleService;
    }

    /**
     * builds Address from webAddress
     *
     * @param webAddress Address Information
     * @return address
     */
    public Address buildAddress(final WebAddress webAddress) {
        return Address.builder()
                .street(webAddress.getStreet())
                .houseNumber(webAddress.getNumber())
                .city(webAddress.getCity())
                .zipcode(webAddress.getZipcode())
                .country(webAddress.getCountry())
                .build();
    }

    /**
     * builds Certificate from webCertificate
     *
     * @param webCertificate informatons
     * @return fully build Certificate
     */
    public Certificate buildCertificate(final WebCertificate webCertificate) {
        return Certificate.builder()
                .name(webCertificate.getGraduation())
                .course(webCertificate.getGraduationcourse())
                .build();
    }

    /**
     * builds Applicant from webApplicant with Address and uniserial as ID
     *
     * @param uniserial    the ID (Name)
     * @param webApplicant Applicant Information
     * @param address      builded Address
     * @param certificate  certificate (highest)
     * @param givenName    first name.
     * @param familyName   surname.
     * @return fully functional Applicant
     */
    public Applicant buildApplicant(final String uniserial, final WebApplicant webApplicant,
                                    final Address address, final Certificate certificate, final String givenName,
                                    final String familyName) {

        Applicant applicant = applicantService.findByUniserial(uniserial);

        if (applicant == null) {

            return Applicant.builder()
                    .uniserial(uniserial)
                    .firstName(givenName)
                    .surname(familyName)
                    .address(address)
                    .birthday(webApplicant.getBirthday())
                    .birthplace(webApplicant.getBirthplace())
                    .gender(webApplicant.getGender())
                    .nationality(webApplicant.getNationality())
                    .course(webApplicant.getCourse())
                    .status(webApplicant.getStatus())
                    .certs(certificate)
                    .comment(webApplicant.getComment())
                    .build();
        }
        ApplicantBuilder applicantBuilder = applicant.toBuilder();
        return applicantBuilder.birthday(webApplicant.getBirthday())
                .birthplace(webApplicant.getBirthplace())
                .address(address)
                .gender(webApplicant.getGender())
                .nationality(webApplicant.getNationality())
                .course(webApplicant.getCourse())
                .status(webApplicant.getStatus())
                .certs(certificate)
                .comment(webApplicant.getComment())
                .build();
    }

    /**
     * Updates Applicant without changing his applications
     *
     * @param newApplicant The Object containing the new information
     */
    public void updateApplicantWithoutChangingApplications(final Applicant newApplicant) {
        Applicant oldApplicant = applicantService.findByUniserial(newApplicant.getUniserial());
        applicantService.saveApplicant(Applicant.builder()
                .applications(oldApplicant.getApplications())
                .uniserial(newApplicant.getUniserial())
                .certs(newApplicant.getCerts())
                .status(newApplicant.getStatus())
                .course(newApplicant.getCourse())
                .nationality(newApplicant.getComment())
                .birthday(newApplicant.getBirthday())
                .address(newApplicant.getAddress())
                .birthplace(newApplicant.getBirthplace())
                .comment(newApplicant.getComment())
                .surname(newApplicant.getSurname())
                .firstName(newApplicant.getFirstName())
                .gender(newApplicant.getGender())
                .build());
    }

    /**
     * Returns WepApplicant for Applicant.
     *
     * @param applicant applicant.
     * @return WebApplicant.
     */
    public WebApplicant getExsistingApplicant(final Applicant applicant) {
        return WebApplicant.builder()
                .birthday(applicant.getBirthday())
                .birthplace(applicant.getBirthplace())
                .comment(applicant.getComment())
                .course(applicant.getCourse())
                .gender(applicant.getGender())
                .nationality(applicant.getNationality())
                .status(applicant.getStatus())
                .build();
    }

    /**
     * Returns web version of Address.
     *
     * @param address address.
     * @return webaddress.
     */
    public WebAddress getExsistingAddress(final Address address) {
        return WebAddress.builder()
                .city(address.getCity())
                .number(address.getHouseNumber())
                .street(address.getStreet())
                .country(address.getCountry())
                .zipcode(address.getZipcode())
                .build();
    }

    /**
     * Returns web version of certiuficate
     *
     * @param certificate certificate
     * @return webcertificate
     */
    public WebCertificate getExsistingCertificate(final Certificate certificate) {
        return WebCertificate.builder()
                .graduationcourse(certificate.getCourse())
                .graduation(certificate.getName())
                .build();
    }

    /**
     * Returns a Set of all Modules the Applicant has not submitted an application yet.
     *
     * @param applicant Applicant.
     * @param modules   all Modules
     * @return Set of Modules.
     */
    public List<Module> getAllNotfilledModules(final Applicant applicant, final List<Module> modules) {
        for (Application app : applicant.getApplications()) {
            modules.remove(app.getModule());
        }
        return modules;
    }

    /**
     * builds Application from webApplication
     *
     * @param webApplication Informations
     * @return fully buildApplication
     */
    public Application buildApplication(final WebApplication webApplication) {
        return Application.builder()
                //Module wird irgendwie nicht eingelesen? Mach ich später >_>
                .module(moduleService.findModuleByName(webApplication.getModule()))
                .minHours(webApplication.getMinHours())//HTML anpassen
                .maxHours(webApplication.getMaxHours())//HTML anpassen
                .priority(webApplication.getPriority())
                .grade(webApplication.getGrade())
                .lecturer(webApplication.getLecturer())
                .semester(webApplication.getSemester())
                .role(webApplication.getRole())
                .comment(webApplication.getComment())
                .build();
    }

    /**
     * Modifies application to the changes in webApplication.
     *
     * @param webApplication data to change.
     * @param application    Merging data into application
     * @return new application with changed data.
     */
    public Application changeApplication(final WebApplication webApplication, final Application application) {
        Application.ApplicationBuilder applicationBuilder = application.toBuilder();
        return applicationBuilder
                .maxHours(webApplication.getMaxHours())
                .minHours(webApplication.getMinHours())
                .semester(webApplication.getSemester())
                .comment(webApplication.getComment())
                .grade(webApplication.getGrade())
                .lecturer(webApplication.getLecturer())
                .role(webApplication.getRole())
                .priority(webApplication.getPriority())
                .build();
    }

    /**
     * Saves personal data of applicant and returns matching applicant
     *
     * @param token          keycloak
     * @param webApplicant   Applicant data
     * @param webAddress     Address data
     * @param webCertificate Certificate data
     * @return applicant
     */
    public Applicant savePersonalData(final KeycloakAuthenticationToken token, final WebApplicant webApplicant,
                                      final WebAddress webAddress, final WebCertificate webCertificate) {
        OidcKeycloakAccount account = token.getAccount();
        String givenName = account.getKeycloakSecurityContext().getIdToken().getGivenName();
        String familyName = account.getKeycloakSecurityContext().getIdToken().getFamilyName();
        Address address = buildAddress(webAddress);
        Certificate certificate = buildCertificate(webCertificate);
        Applicant applicant = buildApplicant(token.getName(), webApplicant,
                address, certificate, givenName, familyName);
        applicantService.saveApplicant(applicant);
        return applicant;
    }
}
