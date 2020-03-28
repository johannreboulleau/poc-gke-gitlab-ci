package com.example.demo.controllers;

import com.example.demo.properties.MailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@RestController
@RequestMapping("/mail")
@Slf4j
public class MailRestController {

    @Autowired
    private MailProperties mailProperties;

    @RequestMapping("/send")
    public void sendEmail() {

        log.info("sendEmail");

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getPersonalFrom()));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("studio.hotel.lille@gmail.com", "Mr. User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText("This is a test");
            Transport.send(msg);
        } catch (AddressException e) {
            log.error("AddressException", e);
        } catch (MessagingException e) {
            log.error("MessagingException", e);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
        }
    }
}
