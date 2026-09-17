package com.school.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // После входа отправляем пользователя на его раздел в зависимости от роли.
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String target = "/student";
            for (GrantedAuthority a : authentication.getAuthorities()) {
                String role = a.getAuthority();
                if (role.equals("ROLE_ADMIN")) { target = "/admin"; break; }
                if (role.equals("ROLE_TEACHER")) { target = "/groups"; break; }
            }
            response.sendRedirect(request.getContextPath() + target);
        };
    }

    // Доступ запрещён (в т.ч. протухший CSRF-токен на входе): не показываем Whitelabel.
    // Гость → обратно на /login с подсказкой; авторизованный без прав → страница ошибки 403.
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean anonymous = auth == null || auth instanceof AnonymousAuthenticationToken;
            if (anonymous) {
                response.sendRedirect(request.getContextPath() + "/login?expired");
            } else {
                response.sendError(403);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler,
                                           AccessDeniedHandler accessDeniedHandler) throws Exception {
        http
            .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/forgot", "/reset", "/css/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Единая вкладка «Группы» — для преподавателей и админа.
                .requestMatchers("/groups/**").hasAnyRole("TEACHER", "ADMIN")
                // Администратор имеет полный доступ, поэтому допускается и к страницам урока преподавателя.
                .requestMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers("/student/**").hasRole("STUDENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }
}
