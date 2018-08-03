package com.example.app.products.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MimeMessageBuilder {

    //private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailService.class);
    private static final Logger LOGGER = Logger.getLogger(SendEmailService.class.getName());
    
    private static final String DEFAULT_CHARSET = "ISO-8859-1";

    private static final String DEFAULT_SUBTYPE = "html";

    public static MimeMessage buildMessage(SimpleMailMessage simpleMailMessage, Session session) throws MessagingException {

        if ((simpleMailMessage.getTo() == null || simpleMailMessage.getTo().length == 0) && (simpleMailMessage.getBcc() == null || simpleMailMessage.getBcc().length == 0)) {
            LOGGER.info("Email needs one or more recipients");
            throw new MessagingException("Email needs one or more recipients");
        }

        List<InternetAddress> toEmailAddresses = new ArrayList<>();
        if (simpleMailMessage.getTo() != null && simpleMailMessage.getTo().length > 0) {
            for (String toEmailAddress : simpleMailMessage.getTo()) {
                try {
                    toEmailAddresses.add(new InternetAddress(toEmailAddress));
                } catch (AddressException e) {
                    LOGGER.info("Error constructing e-mail addresses from: " + simpleMailMessage.getTo() + "" +e);
                    throw e;
                }
            }
        }

        MimeMessage message = new MimeMessage(session);
        message.setFrom(simpleMailMessage.getFrom());
        message.addRecipients(Message.RecipientType.TO, toEmailAddresses.toArray(new InternetAddress[toEmailAddresses.size()]));

        if (simpleMailMessage.getCc() != null) {
            for (String cc : simpleMailMessage.getCc()) {
                message.addRecipients(Message.RecipientType.CC, cc);
            }
        }

        if (simpleMailMessage.getBcc() != null) {
            for (String bcc : simpleMailMessage.getBcc()) {
                message.addRecipients(Message.RecipientType.BCC, bcc);
            }
        }

        message.setSubject(simpleMailMessage.getSubject());

        String htmlText = simpleMailMessage.getText().replace("\n","<br>");
        message.setText(htmlText,DEFAULT_CHARSET,DEFAULT_SUBTYPE);
        return message;
    }
}
