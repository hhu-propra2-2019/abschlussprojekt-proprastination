package mops.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentWithBachelorTest {

    @Test
    void contextLoads() throws IOException {
        Document document = new DocumentWithBachelor();

    }

    @Test
    void save() throws IOException {
        Document document = new DocumentWithBachelor();

        document.save(new File("/tmp/test.pdf"));

        PDDocument document1 = PDDocument.load(new File("/tmp/test.pdf"));
        document1.setAllSecurityToBeRemoved(true);
        PDAcroForm acroForm = document1.getDocumentCatalog().getAcroForm();
    }

    @Test
    void setField() throws IOException {
        Document document = new DocumentWithBachelor();

        document.setField("Tutorentätigkeit", "On");
        document.save(new File("/tmp/test.pdf"));

        PDDocument document1 = PDDocument.load(new File("/tmp/test.pdf"));
        document1.setAllSecurityToBeRemoved(true);
        PDAcroForm acroForm = document1.getDocumentCatalog().getAcroForm();

        assertThat(acroForm.getField("Tutorentätigkeit").getValueAsString()).isEqualTo("On");

    }

    @Test
    void setGender() throws IOException {
        Document document = new DocumentWithBachelor();

        document.setGender("weiblich");
        document.save(new File("/tmp/test.pdf"));

        PDDocument document1 = PDDocument.load(new File("/tmp/test.pdf"));
        document1.setAllSecurityToBeRemoved(true);
        PDAcroForm acroForm = document1.getDocumentCatalog().getAcroForm();

        assertThat(acroForm.getField("Geschlecht").getValueAsString()).isEqualTo("männlich");

    }

    @Test
    void addGeneralInfos() throws IOException {
        Document document = new DocumentWithBachelor();
        document.addGeneralInfos();
        document.save(new File("/tmp/test.pdf"));

        PDDocument document1 = PDDocument.load(new File("/tmp/test.pdf"));
        document1.setAllSecurityToBeRemoved(true);
        PDAcroForm acroForm = document1.getDocumentCatalog().getAcroForm();


        assertThat(acroForm.getField("Tutorentätigkeit").getValueAsString()).isEqualTo("On");
        assertThat(acroForm.getField("Immatrikulation").getValueAsString()).isEqualTo("On");
        assertThat(acroForm.getField("Group3").getValueAsString()).isEqualTo("6");
        assertThat(acroForm.getField("sonstiges").getValueAsString()).isEqualTo("Veranstaltungsgebundene Tutorentätigkeit"
                + "                                                                 ");
        assertThat(acroForm.getField("Antragsdatum").getValueAsString()).isEqualTo(new SimpleDateFormat("dd/MM/yyyy").format(new Date())
                + "                                                     ");
    }
}