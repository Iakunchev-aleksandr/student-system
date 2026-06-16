package com.school.web;

import com.school.model.AppUser;
import com.school.service.CurrentUserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

// Добавляет текущего пользователя ("me") в модель каждой страницы — для шапки/навигации.
@ControllerAdvice
public class GlobalModelAdvice {

    private final CurrentUserService currentUser;

    public GlobalModelAdvice(CurrentUserService currentUser) {
        this.currentUser = currentUser;
    }

    @ModelAttribute("me")
    public AppUser me(Principal principal) {
        if (principal == null) {
            return null;
        }
        try {
            return currentUser.require(principal);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
