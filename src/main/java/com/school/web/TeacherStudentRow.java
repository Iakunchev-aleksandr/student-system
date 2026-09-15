package com.school.web;

import com.school.model.AppUser;
import com.school.model.AttendanceStatus;

// Строка студента на экране урока преподавателя (с текущими оценкой и статусом).
public class TeacherStudentRow {
    private final AppUser student;
    private final Integer grade;
    private final AttendanceStatus attendance;

    public TeacherStudentRow(AppUser student, Integer grade, AttendanceStatus attendance) {
        this.student = student;
        this.grade = grade;
        this.attendance = attendance;
    }

    public AppUser getStudent() { return student; }
    public Integer getGrade() { return grade; }
    public String getGradeLetter() { return GradeScale.toLetter(grade); }
    public AttendanceStatus getAttendance() { return attendance; }
    public String getAttendanceName() { return attendance == null ? "" : attendance.name(); }
}
