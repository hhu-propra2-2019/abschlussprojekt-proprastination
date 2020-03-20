package mops.services;

import mops.model.Document;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PDFServiceTest {

    @Autowired
    PDFService service;

    Document document;


    @Test
    void generatePDF() throws IOException {
        Application application = Application.builder()
                .module("Divination")
                .priority(1)
                .role("Tutor")
                .comment("asd")
                .semester("SS2020")
                .hours(17)
                .build();
        Address address = Address.builder()
                .street("Baker Street")
                .houseNumber("21B")
                .city("London")
                .country("England")
                .zipcode(20394)
                .build();

        Applicant applicant = Applicant.builder()
                .uniserial("wau")
                .surname("J")
                .firstName("WAUWAU")
                .title("")
                .address(address)
                .birthday("01.01.2001")
                .birthplace("Wakanda")
                .course("Arts")
                .nationality("English")
                .status("New")
                .application(application)
                .build();

        String file = service.generatePDF(application, applicant);

        PDDocument document1 = PDDocument.load(new File(file));
        document1.setAllSecurityToBeRemoved(true);
        PDAcroForm acroForm = document1.getDocumentCatalog().getAcroForm();

        assertThat(acroForm.getField("E-Mail").getValueAsString()).isEqualTo("wau@hhu.de");
        assertThat(acroForm.getField("Vorname").getValueAsString()).isEqualTo("WAUWAU");
        assertThat(acroForm.getField("Name").getValueAsString()).isEqualTo("J");
        assertThat(acroForm.getField("Anschrift (Straße)").getValueAsString()).isEqualTo("Baker Street");
        assertThat(acroForm.getField("Stunden").getValueAsString()).isEqualTo("17");
        assertThat(acroForm.getField("Studiengang").getValueAsString()).isEqualTo("Arts");

    }
}