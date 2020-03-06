package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CertificateTest {

    @Test
    void builder() {
        Certificate cert = Certificate.builder()
                .name("Blau")
                .university("HHU")
                .build();

        assertThat(cert)
                .hasFieldOrPropertyWithValue("name", "Blau")
                .hasFieldOrPropertyWithValue("university", "HHU");
    }

    @Test
    void testEquals() {
        Certificate cert1 = Certificate.builder()
                .name("Blau")
                .university("HHU")
                .build();

        Certificate cert2 = Certificate.builder()
                .name("Blau")
                .university("HHU")
                .build();

        assertThat(cert1).isEqualTo(cert2);
    }

    @Test
    void testToString() {
        Certificate cert = Certificate.builder()
                .name("Tom")
                .university("HHU")
                .build();
        assertThat(cert.toString()).isEqualTo("Certificate(name=Tom, university=HHU)");
    }

    @Test
    void testToBuilder() {
        Certificate cert = Certificate.builder()
                .name("Blau")
                .university("HHU")
                .build();

        Certificate.CertificateBuilder certificateBuilder = cert.toBuilder();
        Certificate cert2 = certificateBuilder.build();

        assertThat(cert2).isEqualTo(cert);

    }
}