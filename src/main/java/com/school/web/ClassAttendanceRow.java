package com.school.web;

import com.school.model.AppUser;
import com.school.model.AttendanceStatus;

import java.util.List;

// Строка сводки посещаемости класса за неделю: студент, статусы по каждому уроку,
// число присутствий (出席+遅刻) и всего уроков.
public class ClassAttendanceRow {
    private final AppUser student;
    private final List<AttendanceStatus> cells;
    private final int present;
    private final int total;

    public ClassAttendanceRow(AppUser student, List<AttendanceStatus> cells, int present, int total) {
        this.student = student;
        this.cells = cells;
        this.present = present;
        this.total = total;
    }

    public AppUser getStudent() { return student; }
    public List<AttendanceStatus> getCells() { return cells; }
    public int getPresent() { return present; }
    public int getTotal() { return total; }
}
