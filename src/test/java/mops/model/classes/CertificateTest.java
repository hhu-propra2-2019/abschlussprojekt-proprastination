package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CertificateTest {

    @Test
    void builder() {
        Certificate cert = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        assertThat(cert)
                .hasFieldOrPropertyWithValue("name", "Blau")
                .hasFieldOrPropertyWithValue("course", "HHU");
    }

    @Test
    void testEquals() {
        Certificate cert1 = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        Certificate cert2 = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        assertThat(cert1).isEqualTo(cert2);
    }

    @Test
    void testToString() {
        Certificate cert = Certificate.builder()
                .name("Tom")
                .course("HHU")
                .build();
        assertThat(cert.toString()).isEqualTo("Certificate(name=Tom, course=HHU)");
    }

}