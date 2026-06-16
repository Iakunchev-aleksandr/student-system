package com.school.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
                if (role.equals("ROLE_TEACHER")) { target = "/teacher"; break; }
            }
            response.sendRedirect(request.getContextPath() + target);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
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
