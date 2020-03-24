package mops.testenviroment;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import com.github.javafaker.Faker;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Distribution;
import mops.model.classes.Evaluation;
import mops.model.classes.Module;
import mops.model.classes.Priority;
import mops.model.classes.Role;
import mops.repositories.ApplicantRepository;
import mops.repositories.ApplicationRepository;
import mops.repositories.DistributionRepository;
import mops.repositories.EvaluationRepository;
import mops.repositories.ModuleRepository;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("checkstyle:MagicNumber")
@Component
public class DatabaseInit implements ServletContextInitializer {
    private static final int ENTRYNUMBER = 30;
    private transient Random random = new Random();

    private transient ApplicantRepository applicantRepository;

    private transient ApplicationRepository applicationRepository;

    private transient DistributionRepository distributionRepository;

    private transient EvaluationRepository evaluationRepository;

    private transient ModuleRepository moduleRepository;


    /**
     * Inits.
     *
     * @param applicantRepository    a
     * @param applicationRepository  b
     * @param distributionRepository c
     * @param evaluationRepository   d
     * @param moduleRepository       e
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DatabaseInit(final ApplicantRepository applicantRepository,
                        final ApplicationRepository applicationRepository,
                        final DistributionRepository distributionRepository,
                        final EvaluationRepository evaluationRepository,
                        final ModuleRepository moduleRepository) {
        this.applicantRepository = applicantRepository;
        this.applicationRepository = applicationRepository;
        this.distributionRepository = distributionRepository;
        this.evaluationRepository = evaluationRepository;
        this.moduleRepository = moduleRepository;
    }


    /**
     * On statup function.
     *
     * @param servletContext context.
     */
    public void onStartup(final ServletContext servletContext) {
        Faker faker = new Faker(Locale.GERMAN);
        fakeModules(faker);
        fakeApplicants(faker);
        fakeEvaluations(faker);
        fakeDistribution();
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
            Module[] modules = nextModules();

            Application application1 = Application.builder()
                    .module(modules[0])
                    .minHours(faker.number().numberBetween(1, 10))
                    .maxHours(faker.number().numberBetween(10, 17))
                    .finalHours(nextFinalHour())
                    .lecturer(faker.name().fullName())
                    .grade(nextGrade())
                    .semester("SS2020")
                    .comment(truncate(faker.rickAndMorty().quote(), 255))
                    .role(getRole())
                    .priority(nextPriority())
                    .build();

            Application application2 = Application.builder()
                    .module(modules[1])
                    .minHours(faker.number().numberBetween(1, 10))
                    .maxHours(faker.number().numberBetween(10, 17))
                    .finalHours(nextFinalHour())
                    .lecturer(faker.name().fullName())
                    .grade(nextGrade())
                    .semester("SS2020")
                    .comment(truncate(faker.rickAndMorty().quote(), 255))
                    .role(getRole())
                    .priority(nextPriority())
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
        applicantRepository.save(createMainRole("studentin", faker));

    }

    private Module[] nextModules() {
        long value = random.nextInt(3) + 1;
        long value2 = random.nextInt(3) + 1;
        while (value == value2) {
            value2 = random.nextInt(3) + 1;
        }
        Module[] modules = new Module[2];
        modules[0] = moduleRepository.findById(value).get();
        modules[1] = moduleRepository.findById(value2).get();
        return modules;
    }

    private String nextGender() {
        if (random.nextBoolean()) {
            return "männlich";
        }
        return "weiblich";
    }

    private Priority nextPriority() {
        Priority prio;
        switch (random.nextInt(4)) {
            case 0:
                prio = Priority.SehrHoch;
                break;
            case 1:
                prio = Priority.Hoch;
                break;
            case 2:
                prio = Priority.Neutral;
                break;
            default:
                prio = Priority.Negative;
                break;
        }
        return prio;
    }

    private int nextFinalHour() {
        int[] hours = {7, 9, 17};
        return hours[random.nextInt(3)];
    }

    private double nextGrade() {
        double[] grades = {1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 5.0};
        return grades[random.nextInt(11)];
    }

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
    private Role getRole() {
        Role ret;
        switch (random.nextInt(3)) {
            case 0:
                ret = Role.KORREKTOR;
                break;
            case 1:
                ret = Role.TUTOR;
                break;
            default:
                ret = Role.NONE;
                break;
        }
        return ret;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private Applicant createMainRole(final String role, final Faker faker) {
        Address address = Address.builder()
                .street(faker.address().streetName())
                .houseNumber(faker.address().buildingNumber())
                .city(faker.address().city())
                .country(faker.address().country())
                .zipcode(faker.number().numberBetween(10000, 99999))
                .build();

        Certificate certificate = Certificate.builder()
                .course(faker.educator().course())
                .name("Bachelor")
                .build();

        Module[] modules = nextModules();

        Application application1 = Application.builder()
                .module(modules[0])
                .finalHours(nextFinalHour())
                .minHours(faker.number().numberBetween(1, 10))
                .maxHours(faker.number().numberBetween(10, 17))
                .lecturer(faker.name().fullName())
                .grade(nextGrade())
                .semester("SS2020")
                .comment(truncate(faker.rickAndMorty().quote(), 255))
                .role(getRole())
                .priority(nextPriority())
                .build();

        Application application2 = Application.builder()
                .module(modules[1])
                .finalHours(nextFinalHour())
                .minHours(faker.number().numberBetween(1, 10))
                .maxHours(faker.number().numberBetween(10, 17))
                .lecturer(faker.name().fullName())
                .grade(nextGrade())
                .semester("SS2020")
                .comment(truncate(faker.rickAndMorty().quote(), 255))
                .role(getRole())
                .priority(nextPriority())
                .build();

        return Applicant.builder()
                .uniserial(role)
                .firstName(role)
                .surname(role)
                .comment(truncate(faker.yoda().quote(), 255))
                .course("Informatik")
                .nationality(faker.nation().nationality())
                .birthday(getDate(faker.date().birthday()))
                .status("Einstellung")
                .application(application1)
                .application(application2)
                .birthplace(faker.address().country())
                .gender(nextGender())
                .certs(certificate)
                .address(address)
                .build();
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private void fakeDistribution() {
        int i = 0;
        List<Applicant> applications = applicantRepository.findAll();
        List<Distribution> all = new ArrayList<>();
        List<Module> modules = moduleRepository.findAll();
        int size = applications.size();
        int msize = modules.size();
        int part = size / msize;
        for (int x = 0; x < msize; x++) {
            Collection<Applicant> apps = new ArrayList<>();
            for (int j = 0; j < part; j++) {
                apps.add(applications.get(j + (x * part)));
            }
            Distribution distribution = Distribution.builder()
                    .module(modules.get(x))
                    .employees(apps)
                    .build();
            all.add(distribution);
        }
        distributionRepository.saveAll(all);

    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private void fakeEvaluations(final Faker faker) {
        applicationRepository.findAll().forEach(application -> {
                    Evaluation evaluation = Evaluation.builder()
                            .comment(truncate(faker.yoda().quote(), 255))
                            .hours(faker.number().numberBetween(7, 17))
                            .priority(nextPriority())
                            .application(application)
                            .build();
                    evaluationRepository.save(evaluation);
                }
        );
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private void fakeModules(final Faker faker) {
        String[] modulenames = {"Programmier Praktikum 1", "Programmier Praktikum 2",
                "Rechner, Daten, Betrieb"};
        String[] shortNames = {"ProPra1", "Propra2", "RDB"};
        String[] profNames = {"Jens", "Chris", "Ursula"};
        String[] sevenHour = {"1", "2", "3"};
        String[] nineHour = {"11", "22", "33"};
        String[] seventeenHour = {"111", "222", "333"};
        String[] hour = {"0", "01", "02"};
        for (int i = 0; i < modulenames.length; i++) {
            Instant date = faker.date().future(300, 30, TimeUnit.DAYS).toInstant();
            Module module = Module.builder()
                    .name(modulenames[i])
                    .shortName(shortNames[i])
                    .profName(profNames[i])
                    .sevenHourLimit(sevenHour[i])
                    .nineHourLimit(nineHour[i])
                    .seventeenHourLimit(seventeenHour[i])
                    .hourLimit(hour[i])
                    .deadline(date)
                    .build();
            moduleRepository.save(module);
        }
    }
}
