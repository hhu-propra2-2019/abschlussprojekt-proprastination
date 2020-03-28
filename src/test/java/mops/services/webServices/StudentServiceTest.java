package mops.services.webServices;

import mops.model.classes.Address;
import mops.model.classes.webclasses.WebAddress;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ModuleService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StudentServiceTest {

    private ApplicantService applicantServiceMock = mock(ApplicantService.class);
    private ModuleService moduleServiceMock = mock(ModuleService.class);

    private  StudentService studentService = new StudentService(applicantServiceMock, moduleServiceMock);

    private WebAddress webAddress = WebAddress.builder()
            .city("M-Stadt")
            .country("Musterania")
            .number("42")
            .street("Musterstraße")
            .zipcode("77777")
            .build();

    private Address address = Address.builder()
            .street("Musterstraße")
            .city("M-Stadt")
            .country("Musterania")
            .houseNumber("42")
            .zipcode("77777")
            .build();

    @Test
    void buildAddress() {
        Address result = studentService.buildAddress(webAddress);

        assertEquals(result, address);
    }



}