package mops.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EMailService {

    private Logger logger = LoggerFactory.getLogger(EMailService.class);

    @Value("${spring.mail.username}")
    private String senderEmail = "dummy@example.com";

    private JavaMailSender emailSender;
    private static final int MBSIZE = 1048576;
    //our test mail adress (webmail) is limited to 5 mb attachments,
    //you can change this limit if larger attachments are possible
    private static final int MAXSIZE = 5;

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
            helper.setFrom(senderEmail);
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
     * @param zipfile   Filepath to PDF file.
     */
    public void sendEmailToRecipient(final String recipient, final File zipfile) throws MailSendException {

        try {
            if (zipfile.length() / MBSIZE >= MAXSIZE) {
                throw new MailSendException("Anhang zu groß!");
            }

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail);
            helper.setSubject("Alle Tutorenbewerbung");
            helper.setTo(recipient);
            helper.setText("<!DOCTYPE html>\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<p>Hallo Verteiler!</p>\n"
                    + "<p>Anbei erhalten Sie alle automatisch gernerierten PDFs.</p>\n"
                    + "<p>Liebe Gr&uuml;&szlig;e,</p>\n"
                    + "<p>Ihr Mops-Bwerbungs2-Team!</p>"
                    + "</html>", true);
            FileSystemResource file = new FileSystemResource(zipfile);
            helper.addAttachment("Tutorenbewerbungen.zip", file);
            emailSender.send(message);
        } catch (Exception e) {
            logger.error("Email-Selbstversand fehlgeschlagen an " + recipient + "\n" + e.getMessage());
            throw new MailSendException("Email-Selbstversand fehlgeschlagen an " + recipient + "\n" + e.getMessage());
        }
        logger.info("Email an " + recipient + " erfolgreich versendet.");
    }
}
