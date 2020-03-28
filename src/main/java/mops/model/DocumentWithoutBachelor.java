package mops.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentWithoutBachelor implements Document {

    private static final String ORIGINAL_DOCUMENT = "321_Antrag_Beschaeftigung_stud_Hilfskraefte.pdf";
    private final PDDocument document;
    private final PDAcroForm acroForm;

    /**
     * Initializes Document.
     *
     * @throws IOException IOException.
     */
    public DocumentWithoutBachelor() throws IOException {
        document = PDDocument.load(new File(System.getProperty("user.dir") + File.separator + ORIGINAL_DOCUMENT));
        document.setAllSecurityToBeRemoved(true);
        acroForm = document.getDocumentCatalog().getAcroForm();
    }

    /**
     * Saves File.
     *
     * @param newFile FIle to save.
     * @throws IOException IOException.
     */
    @Override
    public void save(final File newFile) throws IOException {
        document.save(newFile);
        document.close();
    }

    /**
     * Sets field given the parameters.
     *
     * @param fieldName    Name of field.
     * @param fieldContent Fieldcontent.
     * @throws IOException IOException.
     */
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

    /**
     * Adds gerneral that is same for all applicants.
     *
     * @throws IOException IOException.
     */
    @Override
    public void addGeneralInfos() throws IOException {
        setField("Tutorentätigkeit", "On");
        setField("Immatrikulation", "On");
        setField("Antragsdatum", getCurrentDateAsString()
                + "                                                     ");
    }

    private String getCurrentDateAsString() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    /**
     * Prints out all Fieldvalues.
     */
    @Override
    public void debug() {
        acroForm.getFields().forEach(System.out::println);
    }
}
