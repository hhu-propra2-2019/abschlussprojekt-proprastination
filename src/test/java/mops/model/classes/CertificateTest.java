package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testHashCode() {
        Certificate cert1 = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        Certificate cert2 = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        int hashCode1 = cert1.hashCode();
        int hashCode2 = cert2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeDifferentName() {
        Certificate cert1 = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        Certificate cert2 = Certificate.builder()
                .name("Grün")
                .course("HHU")
                .build();

        int hashCode1 = cert1.hashCode();
        int hashCode2 = cert2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeDifferentCourse() {
        Certificate cert1 = Certificate.builder()
                .name("Blau")
                .course("HHU")
                .build();

        Certificate cert2 = Certificate.builder()
                .name("Blau")
                .course("Magic")
                .build();

        int hashCode1 = cert1.hashCode();
        int hashCode2 = cert2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void testNoArgsConstructor() {
        Certificate emptyCertificate = new Certificate();

        assertNull(emptyCertificate.getName());
        assertNull(emptyCertificate.getCourse());
    }

}