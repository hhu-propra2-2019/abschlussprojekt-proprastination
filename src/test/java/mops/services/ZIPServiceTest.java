package mops.services;

import mops.model.classes.Address;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Certificate;
import mops.model.classes.Distribution;
import mops.model.classes.Module;
import mops.model.classes.Organizer;
import mops.model.classes.Priority;
import mops.model.classes.Role;
import mops.model.classes.webclasses.DownloadProgress;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.DbDistributionService;
import mops.services.dbServices.OrganizerService;
import mops.services.logicServices.DistributionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.zip.ZipOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ZIPServiceTest {

    @Mock
    DbDistributionService dbDistributionService;

    @Mock
    PDFService pdfService;

    @Mock
    ApplicantService applicantService;

    @Mock
    OrganizerService organizerService;

    @Mock
    ApplicationService applicationService;


    @InjectMocks
    ZIPService zipService;

    @BeforeEach
    void setup() throws IOException {
        Module module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
                .name("Info4")
                .build();
        Address address = Address.builder()
                .zipcode("12345")
                .country("USA")
                .city("Düsseldorf")
                .street("Street")
                .houseNumber("999")
                .build();

        Certificate certificate = Certificate.builder()
                .name("Bachelor")
                .course("Informatik")
                .build();
        Application application = Application.builder()
                .minHours(2)
                .maxHours(4)
                .grade(1.3)
                .priority(Priority.VERYHIGH)
                .lecturer("Tester")
                .role(Role.PROOFREADER)
                .semester("WS2020")
                .module(module)
                .comment("")
                .build();

        Applicant applicant = Applicant.builder()
                .uniserial("lolol420")
                .address(address)
                .gender("male")
                .firstName("Angelo")
                .surname("Merkel")
                .comment("Moin")
                .nationality("Russian")
                .birthplace("Deutschland")
                .birthday("2000-02-10")
                .course("Trivial")
                .status("Einstellung")
                .application(application)
                .certs(certificate)
                .build();

        Distribution distribution = Distribution.builder()
                .employee(applicant)
                .id(2)
                .module(module)
                .build();

        Organizer organizer = Organizer.builder()
                .uniserial("Test")
                .phonenumber("test")
                .build();


        File file = File.createTempFile("baum", ".pdf");
        Mockito.when(pdfService.generatePDF(any(Application.class), any(Applicant.class), any(Organizer.class))).thenReturn(file);
        Mockito.when(dbDistributionService.findAll()).thenReturn(Arrays.asList(distribution));
        Mockito.when(dbDistributionService.findByModule(any(Module.class))).thenReturn(distribution);
        Mockito.when(applicationService.findApplicationsByModule(any(Module.class))).thenReturn(Arrays.asList(application));
        Mockito.when(applicantService.findByApplications(any(Application.class))).thenReturn(applicant);
        Mockito.when(organizerService.findByUniserial(any())).thenReturn(organizer);


    }

    @Test
    void getZipFileForAllDistributions() throws IOException {
        File file = zipService.getZipFileForAllDistributions(new DownloadProgress());

        assertThat(file).exists();
        assertThat(file).isFile();
        assertThat(file).canRead();
        assertThat(file.getName()).contains("bewerbung");
        assertThat(file.getName()).contains(".zip");
    }

    @Test
    void getZipFileForModule() throws IOException {
        File file = zipService.getZipFileForModule(Arrays.asList(Module.builder().build()), new DownloadProgress());

        assertThat(file).exists();
        assertThat(file).isFile();
        assertThat(file).canRead();
        assertThat(file.getName()).contains("bewerbung");
        assertThat(file.getName()).contains(".zip");
    }

    @Test
    void writeToZipFile() throws IOException {
        File file = File.createTempFile("asdasd", ".tmp");
        FileWriter writer = new FileWriter(file);
        writer.write("Test data");
        writer.close();

        file.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ZipOutputStream zipStream = new ZipOutputStream(fileOutputStream);

        ZIPService.writeToZipFile(file, zipStream, "hallo.txt");

        zipStream.close();
        fileOutputStream.close();
        String everything;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        }

        assertThat(everything).contains("hallo.txt");
    }
}