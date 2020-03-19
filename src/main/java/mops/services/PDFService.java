package mops.services;

import mops.model.Document;
import mops.model.DocumentWithBachelor;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PDFService {

    private Logger logger = LoggerFactory.getLogger(PDFService.class);

    private Document document;

    /**
     * Generates an Application PDF given the parameters.
     *
     * @param application Application.
     * @param applicant   Applicant.
     * @return filepath to file.
     */
    public String generatePDF(final Application application, final Applicant applicant) {
        UUID uuid = UUID.randomUUID();
        String filepath = "/tmp/" + uuid.toString() + ".pdf";

        try {
            document = new DocumentWithBachelor();
            addApplicationInfoToPDF(application);
            addApplicantInfoToPDF(applicant);
            document.addGeneralInfos();
            document.save(new File(filepath));
            logger.debug("Saved PDF to:" + filepath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Saving PDF failed to path: " + filepath);
        }
        return filepath;
    }

    private void addApplicationInfoToPDF(final Application application) throws IOException {
        document.setField("Stunden", String.valueOf(application.getHours()));
        document.setField("E-Mail", application.getApplicantusername() + "@hhu.de");
    }

    private void addApplicantInfoToPDF(final Applicant applicant) throws IOException {
        document.setField("Vorname", applicant.getFirstName());
        document.setField("Name", applicant.getSurname());
        document.setField("Vorsatzwort", applicant.getTitle());
        document.setField("Namenszusatz", applicant.getFirstName());
        document.setField("Geburtsdatum", applicant.getBirthday());
        document.setField("Geburtsort", applicant.getBirthplace());
        document.setField("Staatsangehörigkeit", applicant.getNationality());
        document.setField("Studiengang", applicant.getCourse());
        document.setGender("männlich");
        addApplicantAdressInfoToPDF(applicant.getAddress());
    }

    private void addApplicantAdressInfoToPDF(final Address address) throws IOException {
        document.setField("Anschrift (Straße)", address.getStreet());
        document.setField("Anschrift (Hausnummer)", address.getHouseNumber());
        document.setField("Anschrift (PLZ)", String.valueOf(address.getZipcode()));
        document.setField("Anschrift (optionaler Adresszusatz)", address.getCity());
        document.setField("Anschrift (Ort)", address.getCity());
        document.setField("Anschrift (Land)", address.getCountry());
    }
}
