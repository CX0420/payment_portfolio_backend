package org.example.controller;

import org.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public Map<String, Object> sendTestEmail(@RequestParam String to) {
        Map<String, Object> response = new HashMap<>();

        try {
            emailService.sendSimpleEmail(to,
                    "测试邮件",
                    "这是一封通过Java程序发送的测试邮件。\n\n如果你收到这封邮件，说明配置成功了！");

            response.put("success", true);
            response.put("message", "邮件已发送到: " + to);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "发送失败: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/reset-password")
    public Map<String, Object> sendResetPasswordEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            String resetToken = "test-token-" + System.currentTimeMillis();
            emailService.sendPasswordResetEmail(email, resetToken);

            response.put("success", true);
            response.put("message", "密码重置邮件已发送");
            response.put("token", resetToken);  // 仅用于测试

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "发送失败: " + e.getMessage());
        }

        return response;
    }
}