package com.school.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id")
    private AppUser student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    // Оценка по 5-балльной шкале (2..5).
    @Column(nullable = false)
    private Integer value;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getStudent() { return student; }
    public void setStudent(AppUser student) { this.student = student; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }
}
