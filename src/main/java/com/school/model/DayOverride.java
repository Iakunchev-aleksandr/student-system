package com.school.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

// Замена дня (振替): в конкретную дату действует расписание другого дня недели.
// Область действия по специфичности: schoolClass != null → одна группа;
// иначе course != null → весь курс; иначе → глобально (все группы).
@Entity
@Table(name = "day_override")
public class DayOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    // Шаблон какого дня недели применяется в эту дату.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek substituteDayOfWeek;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public DayOfWeek getSubstituteDayOfWeek() { return substituteDayOfWeek; }
    public void setSubstituteDayOfWeek(DayOfWeek substituteDayOfWeek) { this.substituteDayOfWeek = substituteDayOfWeek; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }
}
