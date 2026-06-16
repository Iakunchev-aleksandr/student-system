package com.school.web;

import com.school.model.FileAttachment;
import com.school.repo.FileAttachmentRepository;
import com.school.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import static org.springframework.http.HttpStatus.NOT_FOUND;

// Отдаёт прикреплённые файлы. Доступно любому аутентифицированному пользователю.
@Controller
public class FileController {

    private final FileAttachmentRepository files;
    private final FileStorageService storage;

    public FileController(FileAttachmentRepository files, FileStorageService storage) {
        this.files = files;
        this.storage = storage;
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        FileAttachment fa = files.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Файл не найден"));
        Resource resource = storage.load(fa.getStoredName());
        String name = URLEncoder.encode(fa.getOriginalName(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + name)
                .body(resource);
    }
}
