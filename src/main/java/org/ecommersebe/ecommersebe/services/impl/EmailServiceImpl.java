package org.ecommersebe.ecommersebe.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.services.EmailService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("\"FOREVER\" <nguyenducson2915@gmail.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendEmailWithQR(String to, String subject, String content, byte[] qrCodeImage) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            // Trong hàm sendEmailWithQR, thêm các dòng sau trước khi gửi message
            helper.setFrom("\"SkedEat\" <skedeat@gmail.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            // Thêm attachment như bình thường
            helper.addAttachment("booking_detail_qr_code.png", new ByteArrayResource(qrCodeImage));

            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email with QR code", e);
        }
    }
}
