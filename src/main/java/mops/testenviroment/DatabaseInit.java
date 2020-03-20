package mops.testenviroment;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import com.github.javafaker.Faker;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.repositories.ApplicantRepository;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@Component
public class DatabaseInit implements ServletContextInitializer {
    private static final int ENTRYNUMBER = 30;
    private transient Random random = new Random();

    private transient ApplicantRepository applicantRepository;

    /**
     * Initializes the needed repo.
     *
     * @param applicantRepository repo.
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DatabaseInit(final ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    /**
     * On statup function.
     *
     * @param servletContext context.
     */
    public void onStartup(final ServletContext servletContext) {
        Faker faker = new Faker(Locale.GERMAN);
        fakeApplicants(faker);
    }

    /**
     * Fakes Applicant entrys.
     *
     * @param faker faker.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public void fakeApplicants(final Faker faker) {
        for (int i = 0; i < ENTRYNUMBER; i++) {
            Address address = Address.builder()
                    .street(faker.address().streetName())
                    .houseNumber(faker.address().buildingNumber())
                    .city(faker.address().city())
                    .country(faker.address().country())
                    .zipcode(faker.number().numberBetween(10000, 99999))
                    .build();

            Certificate certificate = Certificate.builder()
                    .course(faker.educator().course())
                    .name(faker.funnyName().name())
                    .build();

            Application application1 = Application.builder()
                    .module(nextModule())
                    .minHours(faker.number().numberBetween(1, 10))
                    .maxHours(faker.number().numberBetween(10, 17))
                    .lecturer(faker.name().fullName())
                    .grade(faker.number().randomDouble(1, 1, 5))
                    .semester("SS2020")
                    .comment(truncate(faker.rickAndMorty().quote(), 255))
                    .role(getRole())
                    .priority(faker.number().numberBetween(1, 4))
                    .build();

            Application application2 = Application.builder()
                    .module(nextModule())
                    .minHours(faker.number().numberBetween(1, 10))
                    .maxHours(faker.number().numberBetween(10, 17))
                    .lecturer(faker.name().fullName())
                    .grade(faker.number().randomDouble(1, 1, 5))
                    .semester("SS2020")
                    .comment(truncate(faker.rickAndMorty().quote(), 255))
                    .role(getRole())
                    .priority(faker.number().numberBetween(1, 4))
                    .build();

            Applicant applicant = Applicant.builder()
                    .uniserial(truncate(faker.animal().name(), 5) + faker.number().digits(3))
                    .firstName(faker.name().firstName())
                    .surname(faker.name().lastName())
                    .comment(truncate(faker.yoda().quote(), 255))
                    .course(faker.educator().course())
                    .nationality(faker.nation().nationality())
                    .birthday(getDate(faker.date().birthday()))
                    .status(getStatus())
                    .application(application1)
                    .application(application2)
                    .birthplace(faker.address().country())
                    .gender(nextGender())
                    .certs(certificate)
                    .address(address)
                    .build();
            applicantRepository.save(applicant);
        }
    }

    private String nextModule() {
        if (random.nextBoolean()) {
            return "ProPra";
        }
        return "TheoInfo";
    }

    private String nextGender() {
        if (random.nextBoolean()) {
            return "männlich";
        }
        return "weiblich";
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private String getStatus() {
        String ret;
        switch (random.nextInt(3)) {
            case 0:
                ret = "Neueinstellung";
                break;
            case 1:
                ret = "Weiterbeschäftigung";
                break;
            default:
                ret = "Änderung";
                break;
        }
        return ret;
    }

    private String truncate(final String s, final int num) {
        return s.substring(0, Math.min(s.length(), num));
    }

    private String getDate(final Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(date);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private String getRole() {
        String ret;
        switch (random.nextInt(3)) {
            case 0:
                ret = "Tutor";
                break;
            case 1:
                ret = "Korrektor";
                break;
            default:
                ret = "Beides";
                break;
        }
        return ret;
    }
}
