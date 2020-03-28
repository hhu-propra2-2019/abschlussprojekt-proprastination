package mops.controllers;

import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerTest {

    @Autowired
    MockMvc mockMvc;

    //FIXME: Test needs to be updated to use database/ mock of database
    @Test
    @WithMockKeycloackAuth(name = "studentin", roles = "studentin")
    public void applicantCanAccessApplicant() throws Exception {
        mockMvc.perform(get("/bewerbung2/bewerber/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockKeycloackAuth(name = "orga", roles = "orga")
    public void organizerCantAccessApplicant() throws Exception {
        mockMvc.perform(get("/bewerbung2/bewerber/"))
                .andExpect(status().isForbidden());
    }


}