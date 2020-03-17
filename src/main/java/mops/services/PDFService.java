package mops.services;

import mops.model.Document;
import mops.model.DocumentWithBachelor;
import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class PDFService {

    private Document document;

    @PostConstruct
    public void test() throws IOException {
        document = new DocumentWithBachelor();
        document.debug();
        document.addGeneralInfos();
        document.save(new File("/home/sotga/Schreibtisch/test.pdf"));
    }

    public void generatePDF(final Application application, final Applicant applicant) throws IOException {
        document = new DocumentWithBachelor();
        addApplicationInfoToPDF(application);
        addApplicantInfoToPDF(applicant);
        document.addGeneralInfos();
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
