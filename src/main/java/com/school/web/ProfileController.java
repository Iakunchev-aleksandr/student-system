package com.school.web;

import com.school.model.AppUser;
import com.school.repo.AppUserRepository;
import com.school.service.CurrentUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

// Личный профиль: любой пользователь может изменить своё имя, фамилию и пароль.
// Логин, роль, группа, номер студента здесь не меняются.
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final CurrentUserService currentUser;
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final Messages messages;

    public ProfileController(CurrentUserService currentUser, AppUserRepository users, PasswordEncoder encoder,
                             Messages messages) {
        this.currentUser = currentUser;
        this.users = users;
        this.encoder = encoder;
        this.messages = messages;
    }

    @GetMapping
    public String profile(Principal principal, Model model) {
        model.addAttribute("profileUser", currentUser.require(principal));
        return "profile";
    }

    @PostMapping
    public String save(@RequestParam String lastName, @RequestParam String firstName,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String password,
                       @RequestParam(required = false) String passwordConfirm,
                       Principal principal, RedirectAttributes ra) {
        AppUser user = currentUser.require(principal);

        if (lastName == null || lastName.isBlank() || firstName == null || firstName.isBlank()) {
            ra.addFlashAttribute("error", messages.get("flash.nameSurnameRequired"));
            return "redirect:/profile";
        }
        user.setLastName(lastName.trim());
        user.setFirstName(firstName.trim());
        user.setEmail(email != null && !email.isBlank() ? email.trim() : null);

        if (password != null && !password.isBlank()) {
            if (!password.equals(passwordConfirm)) {
                ra.addFlashAttribute("error", messages.get("flash.passwordMismatch"));
                return "redirect:/profile";
            }
            user.setPassword(encoder.encode(password));
        }

        users.save(user);
        ra.addFlashAttribute("message", messages.get("flash.profileSaved"));
        return "redirect:/profile";
    }
}
