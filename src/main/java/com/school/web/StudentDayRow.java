package com.school.web;

import com.school.model.AttendanceStatus;
import com.school.model.Lesson;
import com.school.model.TeacherComment;

import java.util.List;

// Строка дня для студента: урок + его оценка, посещаемость и комментарии преподавателя.
public class StudentDayRow {
    private final Lesson lesson;
    private final Integer grade;
    private final AttendanceStatus attendance;
    private final List<TeacherComment> comments;

    public StudentDayRow(Lesson lesson, Integer grade, AttendanceStatus attendance, List<TeacherComment> comments) {
        this.lesson = lesson;
        this.grade = grade;
        this.attendance = attendance;
        this.comments = comments;
    }

    public Lesson getLesson() { return lesson; }
    public Integer getGrade() { return grade; }
    public String getGradeLetter() { return GradeScale.toLetter(grade); }
    public AttendanceStatus getAttendance() { return attendance; }
    public List<TeacherComment> getComments() { return comments; }
}
