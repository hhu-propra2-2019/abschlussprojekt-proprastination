package mops.services;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.webclasses.WebAddress;
import mops.model.classes.webclasses.WebApplicant;
import mops.model.classes.webclasses.WebCertificate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@SuppressWarnings("checkstyle:HiddenField")
public class StudentService {

    private ApplicantService applicantService;

    private ApplicationService applicationService;

    /**
     * Setup the applicantserives
     *
     * @param applicantService   applicant.
     * @param applicationService application
     */
    public StudentService(final ApplicantService applicantService, final ApplicationService applicationService) {
        this.applicantService = applicantService;
        this.applicationService = applicationService;
    }

    /**
     * builds Address from webAddress
     *
     * @param webAddress Address Information
     * @return address
     */
    public Address buildAddress(final WebAddress webAddress) {
        String street = webAddress.getStreet();
        return Address.builder()
                .street(street.substring(0, street.indexOf(' ')))
                .houseNumber(street.substring(street.indexOf(' ') + 1))
                .city(webAddress.getCity())
                .zipcode(webAddress.getZipcode())
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
                .course(webCertificate.getCourse())
                .build();
    }

    /**
     * builds Applicant from webApplicant with Address and uniserial as ID
     *
     * @param uniserial    the ID (Name)
     * @param webApplicant Applicant Information
     * @param address      builded Address
     * @param certificate  certificate (highest)
     * @return fully functional Applicant
     */
    public Applicant buildApplicant(final String uniserial, final WebApplicant webApplicant,
                                    final Address address, final Certificate certificate) {
        Set<Application> applications = new HashSet<>();
        return Applicant.builder()
                .uniserial(uniserial)
                .firstName("")
                .surname("")
                .address(address)
                .birthday(webApplicant.getBirthday())
                .birthplace(webApplicant.getBirthplace())
                .gender(webApplicant.getGender())
                .nationality(webApplicant.getNationality())
                .course(webApplicant.getCourse())
                .status(webApplicant.getStatus())
                .certs(certificate)
                .comment(webApplicant.getComment())
                .applications(applications)
                .build();
    }
}
