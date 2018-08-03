package com.example.app.products.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.log4j.Logger;

public enum SendEmailService {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(SendEmailService.class.getName());

    //private final com.bizzdesk.project.ngcsat.CaptureConfiguration sendEmailConfiguration;
    public String SENDEREMAILID = "email_address_here";
    public String SENDERPASSWORD = "password_here";
    public String MAIL_HOST = "smtp.mailgun.org";
    public String MAIL_HOST_POST = "587";

    public boolean send(SimpleMailMessage email) throws MessagingException {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", MAIL_HOST);
            properties.put("mail.smtp.port", MAIL_HOST_POST);

            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(properties, auth);
            Transport.send(MimeMessageBuilder.buildMessage(email, session));
            LOGGER.info("Mail sent sucessfully to: " + email.getTo());
            return Boolean.TRUE;
        } catch (MessagingException mex) {
            LOGGER.info("Error trying to send email: " + mex);
            return Boolean.FALSE;
        }
    }

    public class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(SENDEREMAILID, SENDERPASSWORD);
        }
    }
}