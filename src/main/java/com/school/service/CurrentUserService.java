package com.school.service;

import com.school.model.AppUser;
import com.school.repo.AppUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CurrentUserService {

    private final AppUserRepository users;

    public CurrentUserService(AppUserRepository users) {
        this.users = users;
    }

    public AppUser require(Principal principal) {
        return users.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
    }
}
