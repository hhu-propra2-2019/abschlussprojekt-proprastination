package mops.services;

import mops.model.Document;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Module;
import mops.model.classes.Priority;
import mops.model.classes.Role;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PDFServiceTest {

    @Autowired
    PDFService service;

    Document document;


    @Test
    void generatePDF() throws IOException {
        Module module = Module.builder()
                .deadline(Instant.ofEpochSecond(100l))
                .name("Info4")
                .build();

        Certificate cert = Certificate.builder()
                .name("Keins").build();

        Application application = Application.builder()
                .module(module)
                .priority(Priority.NEGATIVE)
                .role(Role.TUTOR)
                .comment("asd")
                .semester("SS2020")
                .minHours(7)
                .maxHours(17)
                .build();
        Address address = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode("NW1 6XE")
                .build();

        Applicant applicant = Applicant.builder()
                .uniserial("wau")
                .surname("J")
                .firstName("WAUWAU")
                .address(address)
                .birthday("2001-02-02")
                .birthplace("Wakanda")
                .certs(cert)
                .course("Arts")
                .nationality("English")
                .status("Einstellung")
                .application(application)
                .build();

        File file = service.generatePDF(application, applicant);

        PDDocument document1 = PDDocument.load(file);
        document1.setAllSecurityToBeRemoved(true);
        PDAcroForm acroForm = document1.getDocumentCatalog().getAcroForm();

        assertThat(acroForm.getField("E-Mail").getValueAsString()).isEqualTo("wau@hhu.de");
        assertThat(acroForm.getField("Vorname").getValueAsString()).isEqualTo("WAUWAU");
        assertThat(acroForm.getField("Name").getValueAsString()).isEqualTo("J");
        assertThat(acroForm.getField("Anschrift (Straße)").getValueAsString()).isEqualTo("Baker Street");
        assertThat(acroForm.getField("Stunden").getValueAsString()).isEqualTo("0");
        assertThat(acroForm.getField("Studiengang").getValueAsString()).isEqualTo("Arts");

    }
}