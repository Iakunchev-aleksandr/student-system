package com.school.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;

// Отправка писем. Если SMTP не настроен (пустой spring.mail.host) — ссылка пишется в лог,
// чтобы восстановление можно было проверить и без почтового провайдера.
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final ObjectProvider<JavaMailSender> mailSender;
    private final MessageSource messages;
    private final String host;
    private final String from;

    public MailService(ObjectProvider<JavaMailSender> mailSender, MessageSource messages,
                       @Value("${spring.mail.host:}") String host,
                       @Value("${app.mail.from:no-reply@gakkyu-nisshi.local}") String from) {
        this.mailSender = mailSender;
        this.messages = messages;
        this.host = host;
        this.from = from;
    }

    // Письмо со ссылкой сброса пароля (текст — на японском, языке продукта).
    // Асинхронно: HTTP-запрос /forgot не ждёт SMTP и отвечает мгновенно.
    @Async
    public void sendPasswordReset(String to, String link) {
        String subject = messages.getMessage("mail.reset.subject", null, Locale.JAPANESE);
        String body = messages.getMessage("mail.reset.body", new Object[]{link}, Locale.JAPANESE);

        JavaMailSender sender = mailSender.getIfAvailable();
        if (host == null || host.isBlank() || sender == null) {
            log.warn("[MAIL DISABLED] Password reset link for {}: {}", to, link);
            return;
        }
        try {
            MimeMessage msg = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            sender.send(msg);
            log.info("Password reset email sent to {}", to);
        } catch (Exception e) {
            // Не роняем запрос из-за почты; ссылку дублируем в лог.
            log.error("Failed to send reset email to {} ({}). Link: {}", to, e.getMessage(), link);
        }
    }
}
