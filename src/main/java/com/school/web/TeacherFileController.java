package com.school.web;

import com.school.model.AppUser;
import com.school.model.FileAttachment;
import com.school.model.Lesson;
import com.school.repo.FileAttachmentRepository;
import com.school.repo.LessonRepository;
import com.school.service.AccessService;
import com.school.service.CurrentUserService;
import com.school.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

// Загрузка преподавателем файла к уроку. Доступ — только тому, кто имеет доступ к уроку.
@Controller
public class TeacherFileController {

    private final CurrentUserService currentUser;
    private final AccessService access;
    private final LessonRepository lessons;
    private final FileAttachmentRepository files;
    private final FileStorageService storage;

    public TeacherFileController(CurrentUserService currentUser, AccessService access,
                                 LessonRepository lessons, FileAttachmentRepository files,
                                 FileStorageService storage) {
        this.currentUser = currentUser;
        this.access = access;
        this.lessons = lessons;
        this.files = files;
        this.storage = storage;
    }

    @PostMapping("/teacher/lesson/{id}/file")
    public String upload(@PathVariable Long id, @RequestParam("file") MultipartFile file, Principal principal) {
        AppUser teacher = currentUser.require(principal);
        Lesson lesson = lessons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!access.canAccessLesson(teacher, lesson)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (file != null && !file.isEmpty()) {
            String stored = storage.store(file);
            FileAttachment fa = new FileAttachment();
            fa.setLesson(lesson);
            fa.setUploadedBy(teacher);
            fa.setOriginalName(file.getOriginalFilename());
            fa.setStoredName(stored);
            files.save(fa);
        }
        return "redirect:/teacher/lesson/" + id;
    }
}
