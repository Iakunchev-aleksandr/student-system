package com.school.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Семестр/учебный период (例: 前期, 後期). Недельный шаблон расписания
// привязывается к семестру, и генератор применяет его только внутри [startDate, endDate].
@Entity
@Table(name = "term")
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    // true, если период покрывает указанную дату.
    public boolean covers(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    // Отображаемое название с периодом, напр. «前期 (2026-04-01 — 2026-09-27)».
    @Transient
    public String getLabel() {
        return name + " (" + startDate + " — " + endDate + ")";
    }
}
