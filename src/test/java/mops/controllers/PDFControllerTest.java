package mops.controllers;

import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import mops.services.dbServices.ApplicantService;
import mops.services.PDFService;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class PDFControllerTest {

    @Mock
    static PDFService service;
    @Mock
    static ApplicantService appService;
    @InjectMocks
    static PDFController controller;
    MockMvc mvc;
    @Autowired
    WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * TODO: Test needs to be fixed due to NullPointerException while loading mocked file;
     *
     * @throws Exception e.
     */
    @Ignore("Test needs to be fixed.")
    //@Test
    @WithMockKeycloackAuth(name = "name", roles = "studentin")
    void fileSystemResource() throws Exception {
        Module module = Module.builder()
                .deadline(Instant.ofEpochSecond(100l))
                .name("Info4")
                .build();
        Application application = Application.builder()
                .module(module)
                .build();
        Applicant applicant = Applicant.builder().application(application).build();


        File file = File.createTempFile("xxxx", ".tmp");
        file.deleteOnExit();

        FileWriter writer = new FileWriter(file);
        writer.write("Test data");
        writer.close();

        when(appService.findByUniserial(any(String.class))).thenReturn(applicant);
        when(service.generatePDF(any(Application.class), any(Applicant.class))).thenReturn(file);


        //mvc.perform(get("/bewerbung2/bewerber/pdf/download?module=creatNewApplicantIfNoneWasFound")).andExpect(status().isOk());

        mvc.perform(get("/bewerbung2/pdf/pfdDownload?module=creatNewApplicantIfNoneWasFound?student=name")).andExpect(status().is4xxClientError());

        verify(appService, times(1)).findByUniserial(any(String.class));
        verify(service, times(1)).generatePDF(any(Application.class), any(Applicant.class));
        boolean ignoreme = file.delete();
    }

    @Test
    @WithMockKeycloackAuth(name = "baum", roles = "studentin")
    void noSuchApplication() throws Exception {
        Module module = Module.builder()
                .deadline(Instant.ofEpochSecond(100l))
                .name("Info4")
                .build();
        Application application = Application.builder()
                .module(module)
                .build();
        Applicant applicant = Applicant.builder().application(application).build();

        File file = File.createTempFile("bbbb", ".tmp");
        file.deleteOnExit();

        when(appService.findByUniserial(any(String.class))).thenReturn(applicant);
        when(service.generatePDF(any(Application.class), any(Applicant.class))).thenReturn(file);

        mvc.perform(get("/bewerbung2/pdf/pdfDownload?module=Baum?student=baum")).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockKeycloackAuth(name = "name", roles = "studentin")
    void missingParam() throws Exception {
        mvc.perform(get("/bewerbung2/pdf/pdfDownload")).andExpect(status().isBadRequest());

    }


}