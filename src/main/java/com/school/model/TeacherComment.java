package com.school.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Комментарий преподавателя к уроку, виден студентам этого урока.
@Entity
@Table(name = "teacher_comment")
public class TeacherComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id")
    private AppUser author;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public AppUser getAuthor() { return author; }
    public void setAuthor(AppUser author) { this.author = author; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
