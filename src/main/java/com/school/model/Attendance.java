package com.school.model;

import jakarta.persistence.*;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id")
    private AppUser student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getStudent() { return student; }
    public void setStudent(AppUser student) { this.student = student; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
}
