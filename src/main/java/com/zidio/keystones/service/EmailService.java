package com.zidio.keystones.service;

import com.zidio.keystones.dto.EmailLogsDTO;
import com.zidio.keystones.dto.ForgotPasswordDTO;
import com.zidio.keystones.entity.EmailLogs;
import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.repository.EmailLogRepository;
import com.zidio.keystones.repository.UserAuthRepo;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private EmailLogRepository emailLogRepo;


    public void sendResetPasswordLink(String toMail, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toMail);
        message.setSubject("Reset your Password");

        message.setText(
                "Hello,\n\n" +
                        "We received a request to reset your password.\n\n" +
                        "Click the link below to reset your password:\n" +
                        resetLink +
                        "\n\n" +
                        "This link will expire soon.\n\n" +
                        "If you did not request a password reset, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "Your Application Team"
        );

        sender.send(message);
    }

    public String notify(EmailLogsDTO email) {

        boolean sentStatus = false;

        try {
            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(email.getRecipientMail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody(), true);

            sender.send(message);

            sentStatus = true;

        } catch (Exception e) {
            sentStatus = false;
        }

        EmailLogs logs = new EmailLogs(
                null,
                email.getRecipientMail(),
                email.getSubject(),
                email.getBody(),
                sentStatus,
                LocalDateTime.now()
        );

        emailLogRepo.save(logs);

        return sentStatus ? "Email sent successfully" : "Failed to send email";
    }


}
