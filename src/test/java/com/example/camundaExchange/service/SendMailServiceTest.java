package com.example.camundaExchange.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class SendMailServiceTest {

    @Autowired
    private JavaMailSender mailSender;

    private GreenMail greenMail;

    @BeforeEach
    public void setup() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
    }

    @AfterEach
    public void cleanup() {
        greenMail.stop();
    }

    @Test
    public void testSendEmail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@example.com");
        message.setTo("recipient@example.com");
        message.setSubject("Test Subject");
        message.setText("Test Message");

        mailSender.send(message);

        greenMail.waitForIncomingEmail(5000, 1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Test Subject", receivedMessages[0].getSubject());
        assertEquals("Test Message", receivedMessages[0].getContent().toString().trim());
    }
}
