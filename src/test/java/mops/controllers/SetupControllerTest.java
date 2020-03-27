package mops.controllers;

import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import mops.model.Account;
import mops.model.classes.webclasses.WebModule;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.DeletionService;
import mops.services.dbServices.ModuleService;
import mops.services.webServices.WebModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SetupControllerTest {

    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private WebModuleService webService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicantService applicantService;

    @MockBean
    private ModuleService moduleService;

    @MockBean
    private DeletionService deletionService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testUnauthorized() throws Exception {


        mvc.perform(get("/bewerbung2/setup/")).andExpect(status().isForbidden());
        mvc.perform(get("/bewerbung2/setup/loeschen")).andExpect(status().isForbidden());
        mvc.perform(get("/bewerbung2/setup/loescheAlles")).andExpect(status().isForbidden());
        mvc.perform(get("/bewerbung2/setup/neuesModul")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testAuthorized() throws Exception {


        mvc.perform(get("/bewerbung2/setup/")).andExpect(status().isOk());
        mvc.perform(get("/bewerbung2/setup/loeschen")).andExpect(status().isOk());
        mvc.perform(get("/bewerbung2/setup/loescheAlles")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/bewerbung2/setup/neuesModul")).andExpect(status().isOk());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostEditModule() throws Exception {
        WebModule module = WebModule.builder()
                .name("Test")
                .nineHourLimit("1")
                .profSerial("Test")
                .sevenHourLimit("2")
                .seventeenHourLimit("3")
                .shortName("T")
                .build();

        MultiValueMap<String, String> bodyMap = toFormParams(module, Set.of());
        bodyMap.add("oldName", "Test");


        mvc.perform(post("/bewerbung2/setup/setupMain")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf())
                .params(bodyMap))
                .andExpect(status().isOk());
        verify(webService, atMostOnce()).update(any(WebModule.class), any(String.class));
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostEditModuleRedirect() throws Exception {
        WebModule module = WebModule.builder()
                .name("Test")
                .shortName("T")
                .build();

        MultiValueMap<String, String> bodyMap = toFormParams(module, Set.of());
        bodyMap.add("oldName", "Test");


        mvc.perform(post("/bewerbung2/setup/setupMain")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf())
                .params(bodyMap))
                .andExpect(status().isOk());
        verify(webService, never()).update(any(), any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostEditModuleUnauthorized() throws Exception {
        WebModule module = WebModule.builder()
                .name("Test")
                .shortName("T")
                .build();

        MultiValueMap<String, String> bodyMap = toFormParams(module, Set.of());
        bodyMap.add("oldName", "Test");
        mvc.perform(post("/bewerbung2/setup/setupMain")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf())
                .params(bodyMap))
                .andExpect(status().isForbidden());
        verify(webService, never()).update(any(), any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostSave() throws Exception {
        WebModule module = WebModule.builder()
                .name("Test")
                .nineHourLimit("1")
                .profSerial("Test")
                .sevenHourLimit("2")
                .seventeenHourLimit("3")
                .shortName("T")
                .build();

        MultiValueMap<String, String> bodyMap = toFormParams(module, Set.of());


        mvc.perform(post("/bewerbung2/setup/neuesModul")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf())
                .params(bodyMap))
                .andExpect(status().isOk());
        verify(webService, atMostOnce()).save(any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostSaveUnauthorized() throws Exception {
        mvc.perform(post("/bewerbung2/setup/neuesModul")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf()))
                .andExpect(status().isForbidden());
        verify(webService, never()).save(any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostSave400() throws Exception {
        WebModule module = WebModule.builder()
                .name("Test")
                .nineHourLimit("1")
                .shortName("T")
                .build();

        MultiValueMap<String, String> bodyMap = toFormParams(module, Set.of());


        mvc.perform(post("/bewerbung2/setup/neuesModul")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf())
                .params(bodyMap))
                .andExpect(status().isOk());
        verify(webService, never()).save(any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostDelete() throws Exception {

        String url = "/bewerbung2/setup/deleteModule";

        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .param("nameDelete", "test")
        )
                .andExpect(status().isOk());
        verify(webService, times(1)).deleteOne(any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostDeleteUnauthorized() throws Exception {

        String url = "/bewerbung2/setup/deleteModule";

        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .param("nameDelete", "test")
        )
                .andExpect(status().isForbidden());
        verify(webService, never()).deleteOne(any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostDeleteAll() throws Exception {

        String url = "/bewerbung2/setup/alleModuleLoeschen";

        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());
        verify(webService, atMostOnce()).deleteAll();
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostEditModul() throws Exception {

        String url = "/bewerbung2/setup/modulBearbeiten";

        mvc.perform(post(url)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostEditModulUnauthorized() throws Exception {

        String url = "/bewerbung2/setup/modulBearbeiten";

        mvc.perform(post(url)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostDeleteAllUnauthorized() throws Exception {

        String url = "/bewerbung2/setup/alleModuleLoeschen";

        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isForbidden());
        verify(webService, never()).deleteAll();
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostDeleteModule() throws Exception {

        String url = "/bewerbung2/setup/loescheModul";

        mvc.perform(post(url).with(csrf())
                .param("module", "test"))
                .andExpect(status().is3xxRedirection());
        verify(deletionService, atMostOnce()).deleteModule(any(), any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostDeleteModuleUnauthorized() throws Exception {

        String url = "/bewerbung2/setup/loescheModul";

        mvc.perform(post(url).with(csrf())
                .param("module", "test"))
                .andExpect(status().isForbidden());
        verify(deletionService, never()).deleteModule(any(), any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostDeleteApplicant() throws Exception {

        String url = "/bewerbung2/setup/loescheApplicant";

        mvc.perform(post(url).with(csrf())
                .param("applicant", "test"))
                .andExpect(status().is3xxRedirection());
        verify(deletionService, atMostOnce()).deleteApplicant(any(), any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostDeleteApplicantUnauthorized() throws Exception {

        String url = "/bewerbung2/setup/loescheApplicant";

        mvc.perform(post(url).with(csrf())
                .param("applicant", "test"))
                .andExpect(status().isForbidden());
        verify(deletionService, never()).deleteApplicant(any(), any());
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "setup")
    public void testPostDeleteApplication() throws Exception {

        String url = "/bewerbung2/setup/loescheApplication";

        mvc.perform(post(url).with(csrf())
                .param("application", "1"))
                .andExpect(status().is3xxRedirection());
        verify(deletionService, atMostOnce()).deleteApplication(any(Long.class), any(Account.class));
    }

    @Test
    @WithMockKeycloackAuth(name = "test", roles = "studentin")
    public void testPostDeleteApplicationUnauthorized() throws Exception {

        String url = "/bewerbung2/setup/loescheApplication";

        mvc.perform(post(url).with(csrf())
                .param("application", "1"))
                .andExpect(status().isForbidden());
        verify(deletionService, never()).deleteApplication(any(Long.class), any(Account.class));
    }


    private MultiValueMap<String, String> toFormParams(Object o, Set<String> excludeFields) throws Exception {
        ObjectReader reader = objectMapper.readerFor(Map.class);
        Map<String, String> map = reader.readValue(objectMapper.writeValueAsString(o));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        map.entrySet().stream()
                .filter(e -> !excludeFields.contains(e.getKey()))
                .forEach(e -> multiValueMap.add(e.getKey(), (e.getValue() == null ? "" : e.getValue())));
        return multiValueMap;
    }

}