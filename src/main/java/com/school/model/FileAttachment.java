package com.school.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Файл, прикреплённый преподавателем к уроку. Виден студентам этого урока.
@Entity
@Table(name = "file_attachment")
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uploaded_by_id")
    private AppUser uploadedBy;

    // Оригинальное имя файла (как у пользователя).
    @Column(nullable = false)
    private String originalName;

    // Имя файла на диске (уникальное, чтобы не было коллизий).
    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public AppUser getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(AppUser uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getStoredName() { return storedName; }
    public void setStoredName(String storedName) { this.storedName = storedName; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
