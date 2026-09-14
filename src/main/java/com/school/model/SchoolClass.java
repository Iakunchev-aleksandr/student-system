package com.school.model;

import jakarta.persistence.*;

@Entity
@Table(name = "school_class",
        uniqueConstraints = @UniqueConstraint(columnNames = {"study_year", "group_code"}))
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Год обучения (например, 1) и код группы (например, "A").
    @Column(name = "study_year", nullable = false)
    private Integer studyYear;

    @Column(name = "group_code", nullable = false)
    private String groupCode;

    // Классный руководитель (преподаватель). Может быть не назначен.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supervisor_id")
    private AppUser supervisor;

    // Курс, к которому относится группа (Business / IT / Hotel Management).
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getStudyYear() { return studyYear; }
    public void setStudyYear(Integer studyYear) { this.studyYear = studyYear; }

    public String getGroupCode() { return groupCode; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode; }

    // Отображаемое название в формате «1年A組».
    @Transient
    public String getName() {
        return studyYear + "年" + groupCode + "組";
    }

    public AppUser getSupervisor() { return supervisor; }
    public void setSupervisor(AppUser supervisor) { this.supervisor = supervisor; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
