package com.school.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Заметка студента на конкретный день.
// forTeacher = true означает, что это комментарий, видимый преподавателям дня.
@Entity
@Table(name = "student_note")
public class StudentNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id")
    private AppUser student;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private boolean forTeacher = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getStudent() { return student; }
    public void setStudent(AppUser student) { this.student = student; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isForTeacher() { return forTeacher; }
    public void setForTeacher(boolean forTeacher) { this.forTeacher = forTeacher; }
}
