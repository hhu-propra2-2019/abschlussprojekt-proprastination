package mops.services;

import mops.model.Document;
import mops.model.DocumentWithBachelor;
import mops.model.DocumentWithoutBachelor;
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
        document.setField("Tutorentätigkeit", "On");
        //addApplicantInfoToPDF();
        //addApplicantAdressInfoToPDF();
        //addApplicationInfoToPDF();
    }

    private void addApplicationInfoToPDF(final Application application) throws IOException {
        document.setField("Stunden", String.valueOf(application.getHours()));
        document.setField("E-Mail", application.getApplicantusername() + "@hhu.de");
    }

    public void addApplicantInfoToPDF(final Applicant applicant) throws IOException {
        document.setField("Vorname", applicant.getName());
        document.setField("Name", applicant.getName());
        document.setField("Vorsatzwort", applicant.getName());
        document.setField("Namenszusatz", applicant.getName());
        document.setField("Geburtsdatum", applicant.getBirthday());
        document.setField("Geburtsort", applicant.getBirthplace());
        document.setField("Staatsangehörigkeit", applicant.getNationality());
        document.setField("Studiengang", applicant.getCourse());
        document.setGender("männlich");
        addApplicantAdressInfoToPDF(applicant.getAddress());
    }

    private void addApplicantAdressInfoToPDF(final Address address) throws IOException {
        document.setField("Anschrift (Straße)", address.getStreet());
        document.setField("Anschrift (Hausnummer)", address.getStreet());
        document.setField("Anschrift (PLZ)", String.valueOf(address.getZipcode()));
        document.setField("Anschrift (optionaler Adresszusatz)", address.getCity());
        document.setField("Anschrift (Ort)", address.getCity());
        document.setField("Anschrift (Land)", address.getCountry());
    }
}
