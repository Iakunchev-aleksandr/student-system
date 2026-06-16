package com.school.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String root(Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }
        for (GrantedAuthority a : auth.getAuthorities()) {
            if (a.getAuthority().equals("ROLE_ADMIN")) return "redirect:/admin";
            if (a.getAuthority().equals("ROLE_TEACHER")) return "redirect:/teacher";
        }
        return "redirect:/student";
    }
}
