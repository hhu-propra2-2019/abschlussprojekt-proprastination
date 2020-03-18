package mops.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EMailService {

    private Logger logger = LoggerFactory.getLogger(EMailService.class);

    private JavaMailSender emailSender;

    /**
     * Autowiring the JavaMailSender
     *
     * @param emailSender JavaMailSender
     */
    @SuppressWarnings("checkstyle:HiddenField")
    @Autowired
    public EMailService(final JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends Email with PDF to D3 and reviewers.
     *
     * @param emailCCrecievers String Array von CC Ansprechpartnern;
     * @param sender           String of sender Email.
     * @param filepath         filepath to PDF.
     */
    public void sendEmailToD3(final String[] emailCCrecievers, final String sender, final String filepath) {
        String mainReceiver = "Hilfskraefte-und-Lehrbeauftragte@hhu.de";
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Tutorenbewerbung von " + sender);
            helper.setTo(mainReceiver);
            helper.setCc(emailCCrecievers);
            helper.setText("Anbei erhalten Sie die Tutorenbewerbung von " + sender);
            FileSystemResource file = new FileSystemResource(new File(filepath));
            helper.addAttachment("Anhang", file);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Email-Versand an D3 fehlgeschlagen für " + sender);
        }
        logger.info("Email an " + sender + " erfolgreich versendet.");
    }

    /**
     * Sends PDF via Email to single recipent.
     *
     * @param recipient Recipients Email.
     * @param filepath  Filepath to PDF file.
     */
    public void sendEmailToRecipient(final String recipient, final String filepath) {

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Deine Tutorenbewerbung");
            helper.setTo(recipient);
            helper.setText("Anbei erhalten Sie Ihre automatisch generierte Tutorenbewerbung");

            FileSystemResource file = new FileSystemResource(new File(filepath));
            helper.addAttachment("Anhang", file);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Email-Versand selbstversand fehlgeschlagen an " + recipient);
        }
        logger.info("Email an " + recipient + " erfolgreich versendet.");
    }
}
