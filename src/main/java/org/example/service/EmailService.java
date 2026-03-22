package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送简单文本邮件
     */
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);  // 从配置中读取发件人
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    /**
     * 发送密码重置邮件
     */
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Password Reset Request";
        String content = "Hello,\n\n"
                + "You have requested to reset your password.\n\n"
                + "Please click the link below to reset your password:\n"
                + "http://localhost:8080/reset-password?token=" + resetToken + "\n\n"
                + "This link will expire in 1 hour.\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Your Team";

        sendSimpleEmail(to, subject, content);
    }
}