package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ApplicantTest {

    @Test
    void builder() {
        Certificate[] certs = {Certificate.builder()
                .name("Bachelor")
                .university("Harvard")
                .build()};
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
                .certs(certs)
                .build();

        assertThat(applicant)
                .hasFieldOrPropertyWithValue("name", "J")
                .hasFieldOrPropertyWithValue("birthday", "01.01.2001")
                .hasFieldOrPropertyWithValue("birthplace", "Wakanda")
                .hasFieldOrPropertyWithValue("course", "Arts")
                .hasFieldOrPropertyWithValue("address", address)
                .hasFieldOrPropertyWithValue("nationality", "English")
                .hasFieldOrPropertyWithValue("status", Status.NEW)
                .hasFieldOrPropertyWithValue("certs", certs);

    }

}