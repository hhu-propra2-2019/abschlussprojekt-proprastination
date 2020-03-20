package mops.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EMailServiceTest {

    static EMailService eMailService;
    static JavaMailSender javaMailSender;
    static MimeMessage mimeMessage;

    @BeforeAll
    public static void setup() {
        javaMailSender = mock(JavaMailSender.class);
        eMailService = new EMailService(javaMailSender);
        mimeMessage = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void checkMethodCallToRecipient() {
        String file = System.getProperty("user.dir") + File.separator + "323_Antrag_Beschaeftigung_wiss_Hilfskraefte_mit_BA.pdf";

        eMailService.sendEmailToRecipient("test@example.com", file);

        verify(javaMailSender, atLeastOnce()).send((MimeMessage) any());


    }

    @Test
    void checkMethodCallToD3() {

        String file = System.getProperty("user.dir") + File.separator + "323_Antrag_Beschaeftigung_wiss_Hilfskraefte_mit_BA.pdf";

        String[] array = {"test@example.com", "test2@example.com"};
        eMailService.sendEmailToD3(array, "itsme@example.com", file);

        verify(javaMailSender, atLeastOnce()).send((MimeMessage) any());

    }
}