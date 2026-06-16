package com.school.web;

import com.school.model.*;
import com.school.repo.*;
import com.school.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final CurrentUserService currentUser;
    private final LessonRepository lessons;
    private final GradeRepository grades;
    private final AttendanceRepository attendance;
    private final StudentNoteRepository notes;
    private final TeacherCommentRepository comments;
    private final SchoolEventRepository events;

    public StudentController(CurrentUserService currentUser, LessonRepository lessons, GradeRepository grades,
                             AttendanceRepository attendance, StudentNoteRepository notes,
                             TeacherCommentRepository comments, SchoolEventRepository events) {
        this.currentUser = currentUser;
        this.lessons = lessons;
        this.grades = grades;
        this.attendance = attendance;
        this.notes = notes;
        this.comments = comments;
        this.events = events;
    }

    @GetMapping
    public String week(@RequestParam(defaultValue = "0") int week, Principal principal, Model model) {
        AppUser student = currentUser.require(principal);
        SchoolClass clazz = student.getSchoolClass();

        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusWeeks(week);
        LocalDate sunday = monday.plusDays(6);

        List<WeekDay> days = new ArrayList<>();
        if (clazz != null) {
            List<Lesson> weekLessons = lessons
                    .findBySchoolClassAndDateBetweenOrderByDateAscStartTimeAsc(clazz, monday, sunday);
            List<SchoolEvent> weekEvents = events
                    .findBySchoolClassAndDateBetweenOrderByDate(clazz, monday, sunday);
            for (int i = 0; i < 7; i++) {
                LocalDate d = monday.plusDays(i);
                List<Lesson> dayLessons = weekLessons.stream()
                        .filter(l -> l.getDate().equals(d)).toList();
                long ev = weekEvents.stream().filter(e -> e.getDate().equals(d)).count();
                days.add(new WeekDay(d, dayLessons, (int) ev));
            }
        }

        model.addAttribute("user", student);
        model.addAttribute("days", days);
        model.addAttribute("week", week);
        model.addAttribute("monday", monday);
        model.addAttribute("sunday", sunday);
        return "student/week";
    }

    @GetMapping("/day")
    public String day(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      Principal principal, Model model) {
        AppUser student = currentUser.require(principal);
        SchoolClass clazz = student.getSchoolClass();

        List<StudentDayRow> rows = new ArrayList<>();
        List<SchoolEvent> dayEvents = new ArrayList<>();
        if (clazz != null) {
            for (Lesson lesson : lessons.findBySchoolClassAndDateOrderByStartTime(clazz, date)) {
                Integer grade = grades.findByStudentAndLesson(student, lesson)
                        .map(Grade::getValue).orElse(null);
                AttendanceStatus att = attendance.findByStudentAndLesson(student, lesson)
                        .map(Attendance::getStatus).orElse(null);
                List<TeacherComment> lessonComments = comments.findByLessonOrderByCreatedAt(lesson);
                rows.add(new StudentDayRow(lesson, grade, att, lessonComments));
            }
            dayEvents = events.findBySchoolClassAndDate(clazz, date);
        }

        StudentNote note = notes.findByStudentAndDate(student, date).orElse(null);

        model.addAttribute("user", student);
        model.addAttribute("date", date);
        model.addAttribute("rows", rows);
        model.addAttribute("events", dayEvents);
        model.addAttribute("note", note);
        return "student/day";
    }

    @PostMapping("/note")
    public String saveNote(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam(defaultValue = "") String text,
                           @RequestParam(defaultValue = "false") boolean forTeacher,
                           Principal principal) {
        AppUser student = currentUser.require(principal);
        StudentNote note = notes.findByStudentAndDate(student, date).orElseGet(StudentNote::new);
        note.setStudent(student);
        note.setDate(date);
        note.setText(text);
        note.setForTeacher(forTeacher);
        notes.save(note);
        return "redirect:/student/day?date=" + date;
    }
}
