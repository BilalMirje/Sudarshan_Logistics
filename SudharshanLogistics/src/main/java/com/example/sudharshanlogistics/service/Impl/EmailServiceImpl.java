package com.example.sudharshanlogistics.service.Impl;

import com.example.sudharshanlogistics.service.EmailConfigService;
import com.example.sudharshanlogistics.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import jakarta.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailConfigService emailConfigService;

    @Value("${mail.fallback}")
    private String fallbackEmail;

    @Override
    public void sendInvoiceEmail(List<String> recipients, String subject, String body, byte[] attachment, String attachmentName) {
        var activeConfig = emailConfigService.getActiveConfig();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(activeConfig.getEmail());
        mailSender.setPassword(new String(Base64.getDecoder().decode(activeConfig.getAppPassword())));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        for (String recipient : recipients) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(recipient);
                helper.setSubject(subject);
                helper.setText(body, true);
                helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
                mailSender.send(message);
                log.info("Email sent successfully to {}", recipient);
            } catch (Exception e) {
                log.error("Error sending email to {}: {}", recipient, e.getMessage());
            }
        }
    }
}
