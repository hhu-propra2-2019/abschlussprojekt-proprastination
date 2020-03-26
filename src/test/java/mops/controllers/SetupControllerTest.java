package mops.controllers;

import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import mops.services.WebModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class SetupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockKeycloackAuth(name = "setup", roles = "setup")
    void indexTest() throws Exception {
        mockMvc.perform(get("/bewerbung2/setup/")).andExpect(status().isOk());
    }

    @Test
    @WithMockKeycloackAuth(name = "other", roles = "orga")
    void otherRoleNoAccess() throws Exception {
        mockMvc.perform(get("/bewerbung2/setup/")).andExpect(status().isForbidden());
    }

    @Test
    void postEditedModule() {
    }

    @Test
    void newModule() {
    }

    @Test
    void postNewModule() {
    }

    @Test
    void postEditModule() {
    }

    @Test
    void postDeleteModule() {
    }

    @Test
    void postDeleteAllModule() {
    }
}