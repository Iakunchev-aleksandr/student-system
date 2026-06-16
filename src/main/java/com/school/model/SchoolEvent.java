package com.school.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Событие/уведомление для группы: экзамен или другая активность.
@Entity
@Table(name = "school_event")
public class SchoolEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_id")
    private AppUser createdBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AppUser getCreatedBy() { return createdBy; }
    public void setCreatedBy(AppUser createdBy) { this.createdBy = createdBy; }
}
