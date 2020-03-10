package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DistributionTest {
    Distribution dist;
    List <Applicant> emps;

    @BeforeEach
    void init(){
        Application application = Application.builder().module("Divination").build();
        List<Application> applicationList = Arrays.asList(application);

        Certificate cert = Certificate.builder()
                .name("Bachelor")
                .university("Harvard")
                .build();
        Address address = Address.builder()
                .street("Baker Street 21B")
                .city("London")
                .country("England")
                .zipcode(20394)
                .build();

        Applicant applicant = Applicant.builder()
                .name("J")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status(Status.NEW)
                .cert(cert)
                .applications(applicationList)
                .build();
        emps = Arrays.asList(applicant);
        dist = Distribution.builder()
                .employees(emps)
                .module("ProPra")
                .build();
    }

    @Test
    void testEquals() {
        Distribution.DistributionBuilder distributionBuilder = dist.toBuilder();
        Distribution dist1 = distributionBuilder.build();

        assertThat(dist).isEqualTo(dist1);
    }

    @Test
    void testBuilder() {
        assertThat(dist)
                .hasFieldOrPropertyWithValue("module", "ProPra")
                .hasFieldOrPropertyWithValue("employees", emps);
    }

    @Test
    void toBuilder() {
        Distribution.DistributionBuilder distributionBuilder = dist.toBuilder();
        Distribution dist1 = distributionBuilder.build();

        Distribution.DistributionBuilder distributionBuilder1 = dist1.toBuilder();
        Distribution dist2 = distributionBuilder1.build();

        assertThat(dist1).isEqualTo(dist2);
    }

    @Test
    void testToString() {
        assertThat(dist.toString()).isEqualTo("Distribution(module=ProPra, employees=[Applicant(name=J, birthplace=Wakanda, address=Address(street=Baker Street 21B, city=London, country=England, zipcode=20394), birthday=01.01.2001, nationality=English, course=Arts, status=Status.NEW, cert=Certificate(name=Bachelor, university=Harvard), applications=[Application(hours=0, module=Divination, grade=0.0, lecturer=null, semester=null, comment=null, role=null)])])");
    }
}