package com.school.web;

import com.school.model.AppUser;
import com.school.repo.AppUserRepository;
import com.school.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

// Отдаёт фотографию пользователя. Доступно любому аутентифицированному пользователю.
@Controller
public class UserPhotoController {

    private final AppUserRepository users;
    private final FileStorageService storage;

    public UserPhotoController(AppUserRepository users, FileStorageService storage) {
        this.users = users;
        this.storage = storage;
    }

    @GetMapping("/users/{id}/photo")
    public ResponseEntity<Resource> photo(@PathVariable Long id) {
        AppUser user = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        if (user.getPhotoName() == null || user.getPhotoName().isBlank()) {
            throw new ResponseStatusException(NOT_FOUND, "Фото не загружено");
        }
        Resource resource = storage.load(user.getPhotoName());
        MediaType type = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(type).body(resource);
    }
}
