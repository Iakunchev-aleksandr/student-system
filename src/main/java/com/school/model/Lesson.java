package com.school.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

// Конкретный урок в конкретный день. Сгенерирован из шаблона.
// К нему привязываются оценки, посещаемость и комментарии преподавателя.
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "teacher_id")
    private AppUser teacher;

    private String room;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public AppUser getTeacher() { return teacher; }
    public void setTeacher(AppUser teacher) { this.teacher = teacher; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
