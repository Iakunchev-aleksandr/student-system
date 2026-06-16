package com.school.web;

import com.school.model.AppUser;

import java.util.List;

// Строка сводки оценок: студент, его оценки по урокам недели (выровнены по списку уроков)
// и средний балл за неделю (null, если оценок нет).
public class ClassGradeRow {
    private final AppUser student;
    private final List<Integer> grades;
    private final Double average;

    public ClassGradeRow(AppUser student, List<Integer> grades, Double average) {
        this.student = student;
        this.grades = grades;
        this.average = average;
    }

    public AppUser getStudent() { return student; }
    public List<Integer> getGrades() { return grades; }
    public Double getAverage() { return average; }
}
