package mops.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentWithoutBachelor implements Document {

    private final String ORIGINAL_DOCUMENT = "321_Antrag_Beschaeftigung_stud_Hilfskraefte.pdf";
    private final PDDocument document;
    private final PDAcroForm acroForm;

    public DocumentWithoutBachelor() throws IOException {
        document = PDDocument.load(new File(System.getProperty("user.dir") + File.separator + ORIGINAL_DOCUMENT));
        document.setAllSecurityToBeRemoved(true);
        acroForm = document.getDocumentCatalog().getAcroForm();
    }

    @Override
    public void save(final File newFile) throws IOException {
        document.save(newFile);
    }

    @Override
    public void setField(final String fieldName, final String fieldContent) throws IOException {
        acroForm.getField(fieldName).setValue(fieldContent);
    }

    /**
     * Flips the given gender to compensate a bug in the document
     *
     * @param gender the gender to be flipped
     * @throws IOException ignored
     */
    @Override
    public void setGender(final String gender) throws IOException {
        if (gender.equals("männlich")) {
            acroForm.getField("Geschlecht").setValue("weiblich");
        } else if (gender.equals("weiblich")) {
            acroForm.getField("Geschlecht").setValue("männlich");
        }
    }

    @Override
    public void addGeneralInfos() throws IOException {
        setField("Tutorentätigkeit", "On");
        setField("Immatrikulation", "On");
        setField("Antragsdatum", getCurrentDateAsString()
                + "                                                     ");
    }

    private String getCurrentDateAsString() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    @Override
    public void debug() {
        acroForm.getFields().forEach(System.out::println);
    }
}
