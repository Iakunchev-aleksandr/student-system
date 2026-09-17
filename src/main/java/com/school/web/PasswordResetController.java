package com.school.web;

import com.school.model.AppUser;
import com.school.model.PasswordResetToken;
import com.school.repo.AppUserRepository;
import com.school.service.MailService;
import com.school.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

// Восстановление пароля по email: /forgot (запрос ссылки) и /reset (установка нового пароля).
@Controller
public class PasswordResetController {

    private final AppUserRepository users;
    private final PasswordResetService reset;
    private final MailService mail;
    private final Messages messages;
    private final String baseUrl;

    public PasswordResetController(AppUserRepository users, PasswordResetService reset, MailService mail,
                                   Messages messages, @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.users = users;
        this.reset = reset;
        this.mail = mail;
        this.messages = messages;
        this.baseUrl = baseUrl;
    }

    @GetMapping("/forgot")
    public String forgotForm() {
        return "forgot";
    }

    @PostMapping("/forgot")
    public String forgot(@RequestParam String email) {
        // Не раскрываем, существует ли аккаунт: всегда отвечаем одинаково.
        if (email != null && !email.isBlank()) {
            Optional<AppUser> user = users.findByEmailIgnoreCase(email.trim());
            user.ifPresent(u -> {
                String token = reset.createTokenFor(u);
                mail.sendPasswordReset(u.getEmail(), baseUrl + "/reset?token=" + token);
            });
        }
        return "redirect:/forgot?sent";
    }

    @GetMapping("/reset")
    public String resetForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("valid", reset.findValid(token).isPresent());
        return "reset";
    }

    @PostMapping("/reset")
    public String reset(@RequestParam String token,
                        @RequestParam String password,
                        @RequestParam(required = false) String passwordConfirm,
                        RedirectAttributes ra) {
        Optional<PasswordResetToken> valid = reset.findValid(token);
        if (valid.isEmpty()) {
            return "redirect:/reset?token=" + token;
        }
        if (password == null || password.isBlank()) {
            ra.addFlashAttribute("error", messages.get("flash.passwordRequired"));
            return "redirect:/reset?token=" + token;
        }
        if (!password.equals(passwordConfirm)) {
            ra.addFlashAttribute("error", messages.get("flash.passwordMismatch"));
            return "redirect:/reset?token=" + token;
        }
        reset.resetPassword(valid.get(), password);
        return "redirect:/login?reset";
    }
}
