package com.school.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

// Недельный шаблон: повторяющийся слот расписания.
// Из шаблонов админ генерирует конкретные датированные уроки (Lesson).
@Entity
@Table(name = "schedule_template")
public class ScheduleTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "teacher_id")
    private AppUser teacher;

    // Семестр, к которому относится слот. null = действует всегда (без привязки к периоду).
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_id")
    private Term term;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String room;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public AppUser getTeacher() { return teacher; }
    public void setTeacher(AppUser teacher) { this.teacher = teacher; }

    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
