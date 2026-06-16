package com.school.config;

import com.school.model.*;
import com.school.repo.*;
import com.school.service.ScheduleGenerationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

// Заполняет базу демонстрационными данными при первом запуске (если пользователей ещё нет).
@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository users;
    private final SchoolClassRepository classes;
    private final SubjectRepository subjects;
    private final ScheduleTemplateRepository templates;
    private final LessonRepository lessons;
    private final GradeRepository grades;
    private final AttendanceRepository attendance;
    private final SchoolEventRepository events;
    private final ScheduleGenerationService scheduleGen;
    private final PasswordEncoder encoder;

    public DataSeeder(AppUserRepository users, SchoolClassRepository classes, SubjectRepository subjects,
                      ScheduleTemplateRepository templates, LessonRepository lessons, GradeRepository grades,
                      AttendanceRepository attendance, SchoolEventRepository events,
                      ScheduleGenerationService scheduleGen, PasswordEncoder encoder) {
        this.users = users;
        this.classes = classes;
        this.subjects = subjects;
        this.templates = templates;
        this.lessons = lessons;
        this.grades = grades;
        this.attendance = attendance;
        this.events = events;
        this.scheduleGen = scheduleGen;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            return; // данные уже есть — не пересоздаём
        }

        AppUser admin = user("admin", "admin123", "システム", "管理者", Role.ADMIN, null, null);
        AppUser teacher = user("teacher1", "teacher123", "田中", "一郎", Role.TEACHER, null, null);
        AppUser petrova = user("teacher2", "teacher123", "佐藤", "花子", Role.TEACHER, null, null);

    }

    private AppUser user(String username, String rawPassword, String lastName, String firstName,
                         Role role, SchoolClass clazz, String studentNumber) {
        AppUser u = new AppUser();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setLastName(lastName);
        u.setFirstName(firstName);
        u.setRole(role);
        u.setSchoolClass(clazz);
        u.setStudentNumber(studentNumber);
        return users.save(u);
    }

    private Subject subject(String name) {
        Subject s = new Subject();
        s.setName(name);
        return subjects.save(s);
    }

    private void template(SchoolClass clazz, Subject subj, AppUser teacher, DayOfWeek day,
                          String start, String end, String room) {
        ScheduleTemplate t = new ScheduleTemplate();
        t.setSchoolClass(clazz);
        t.setSubject(subj);
        t.setTeacher(teacher);
        t.setDayOfWeek(day);
        t.setStartTime(LocalTime.parse(start));
        t.setEndTime(LocalTime.parse(end));
        t.setRoom(room);
        templates.save(t);
    }

    private void grade(AppUser student, Lesson lesson, int value) {
        Grade g = new Grade();
        g.setStudent(student);
        g.setLesson(lesson);
        g.setValue(value);
        grades.save(g);
    }

    private void mark(AppUser student, Lesson lesson, AttendanceStatus status) {
        Attendance a = new Attendance();
        a.setStudent(student);
        a.setLesson(lesson);
        a.setStatus(status);
        attendance.save(a);
    }
}
