package com.school.service;

import com.school.model.AppUser;
import com.school.model.PasswordResetToken;
import com.school.repo.AppUserRepository;
import com.school.repo.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokens;
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final int ttlMinutes;

    public PasswordResetService(PasswordResetTokenRepository tokens, AppUserRepository users,
                                PasswordEncoder encoder,
                                @Value("${app.reset.ttl-minutes:30}") int ttlMinutes) {
        this.tokens = tokens;
        this.users = users;
        this.encoder = encoder;
        this.ttlMinutes = ttlMinutes;
    }

    // Создаёт новый одноразовый токен для пользователя (старые токены удаляются).
    @Transactional
    public String createTokenFor(AppUser user) {
        tokens.deleteByUser(user);
        PasswordResetToken t = new PasswordResetToken();
        t.setToken(UUID.randomUUID().toString().replace("-", ""));
        t.setUser(user);
        t.setExpiresAt(LocalDateTime.now().plusMinutes(ttlMinutes));
        t.setUsed(false);
        tokens.save(t);
        return t.getToken();
    }

    // Действующий токен по строке (или пусто, если нет/просрочен/использован).
    public Optional<PasswordResetToken> findValid(String token) {
        return tokens.findByToken(token).filter(PasswordResetToken::isValid);
    }

    // Применяет новый пароль и помечает токен использованным.
    @Transactional
    public void resetPassword(PasswordResetToken token, String rawPassword) {
        AppUser user = token.getUser();
        user.setPassword(encoder.encode(rawPassword));
        users.save(user);
        token.setUsed(true);
        tokens.save(token);
    }
}
