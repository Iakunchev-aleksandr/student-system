package com.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload-dir}") String dir) {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось создать папку для файлов: " + root, e);
        }
    }

    // Сохраняет файл под уникальным именем, возвращает это имя.
    public String store(MultipartFile file) {
        String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot);
        }
        String stored = UUID.randomUUID() + ext;
        try {
            Files.copy(file.getInputStream(), root.resolve(stored));
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить файл", e);
        }
        return stored;
    }

    public Resource load(String storedName) {
        try {
            Path file = root.resolve(storedName).normalize();
            if (!file.startsWith(root)) {
                throw new IllegalArgumentException("Недопустимый путь");
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException("Файл не найден: " + storedName);
            }
            return resource;
        } catch (Exception e) {
            throw new IllegalArgumentException("Файл не найден: " + storedName, e);
        }
    }
}
