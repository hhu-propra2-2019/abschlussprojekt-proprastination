package mops.services;

import mops.db.dto.ApplicantDTO;
import mops.model.classes.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantServiceTest {
    @Autowired
    ApplicantService service;

    @Test
    void saveApplicant() {


        ApplicantDTO dto = new ApplicantDTO();
        dto.setUsername("asdasdasd");
        dto.setDetails("{ 'id'' :1, 'haus':'BLAU'}");

        service.testit(dto);


    }
}