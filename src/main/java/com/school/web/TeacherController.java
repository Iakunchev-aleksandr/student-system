package com.school.web;

import com.school.model.*;
import com.school.repo.*;
import com.school.service.AccessService;
import com.school.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final CurrentUserService currentUser;
    private final AccessService access;
    private final AppUserRepository appUsers;
    private final SchoolClassRepository classes;
    private final LessonRepository lessons;
    private final GradeRepository grades;
    private final AttendanceRepository attendance;
    private final TeacherCommentRepository comments;
    private final SchoolEventRepository events;
    private final StudentNoteRepository notes;

    public TeacherController(CurrentUserService currentUser, AccessService access, AppUserRepository appUsers,
                             SchoolClassRepository classes, LessonRepository lessons, GradeRepository grades,
                             AttendanceRepository attendance, TeacherCommentRepository comments,
                             SchoolEventRepository events, StudentNoteRepository notes) {
        this.currentUser = currentUser;
        this.access = access;
        this.appUsers = appUsers;
        this.classes = classes;
        this.lessons = lessons;
        this.grades = grades;
        this.attendance = attendance;
        this.comments = comments;
        this.events = events;
        this.notes = notes;
    }

    @GetMapping
    public String home(@RequestParam(defaultValue = "0") int week, Principal principal, Model model) {
        AppUser teacher = currentUser.require(principal);
        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusWeeks(week);
        LocalDate sunday = monday.plusDays(6);

        List<Lesson> weekLessons = lessons
                .findByTeacherAndDateBetweenOrderByDateAscStartTimeAsc(teacher, monday, sunday);
        List<WeekDay> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate d = monday.plusDays(i);
            List<Lesson> dayLessons = weekLessons.stream().filter(l -> l.getDate().equals(d)).toList();
            days.add(new WeekDay(d, dayLessons, 0));
        }

        model.addAttribute("user", teacher);
        model.addAttribute("days", days);
        model.addAttribute("week", week);
        model.addAttribute("monday", monday);
        model.addAttribute("sunday", sunday);
        model.addAttribute("supervisedClasses", classes.findBySupervisor(teacher));
        return "teacher/home";
    }

    @GetMapping("/lesson/{id}")
    public String lesson(@PathVariable Long id, Principal principal, Model model) {
        AppUser teacher = currentUser.require(principal);
        Lesson lesson = lessons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!access.canAccessLesson(teacher, lesson)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа к этому уроку");
        }

        List<AppUser> students = appUsers.findBySchoolClassOrderByLastNameAscFirstNameAsc(lesson.getSchoolClass());
        List<TeacherStudentRow> rows = new ArrayList<>();
        for (AppUser s : students) {
            Integer g = grades.findByStudentAndLesson(s, lesson).map(Grade::getValue).orElse(null);
            AttendanceStatus a = attendance.findByStudentAndLesson(s, lesson).map(Attendance::getStatus).orElse(null);
            rows.add(new TeacherStudentRow(s, g, a));
        }

        model.addAttribute("user", teacher);
        model.addAttribute("lesson", lesson);
        model.addAttribute("rows", rows);
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("comments", comments.findByLessonOrderByCreatedAt(lesson));
        model.addAttribute("studentNotes",
                notes.findByStudent_SchoolClassAndDateAndForTeacherTrue(lesson.getSchoolClass(), lesson.getDate()));
        model.addAttribute("eventTypes", EventType.values());
        return "teacher/lesson";
    }

    @PostMapping("/lesson/{id}/grades")
    public String saveGrades(@PathVariable Long id, @RequestParam Map<String, String> params, Principal principal) {
        AppUser teacher = currentUser.require(principal);
        Lesson lesson = lessons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!access.canAccessLesson(teacher, lesson)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        for (AppUser s : appUsers.findBySchoolClassOrderByLastNameAscFirstNameAsc(lesson.getSchoolClass())) {
            String gradeStr = params.get("grade_" + s.getId());
            String attStr = params.get("att_" + s.getId());

            if (gradeStr != null && !gradeStr.isBlank()) {
                Grade g = grades.findByStudentAndLesson(s, lesson).orElseGet(Grade::new);
                g.setStudent(s);
                g.setLesson(lesson);
                g.setValue(Integer.parseInt(gradeStr));
                grades.save(g);
            }
            if (attStr != null && !attStr.isBlank()) {
                Attendance a = attendance.findByStudentAndLesson(s, lesson).orElseGet(Attendance::new);
                a.setStudent(s);
                a.setLesson(lesson);
                a.setStatus(AttendanceStatus.valueOf(attStr));
                attendance.save(a);
            }
        }
        return "redirect:/teacher/lesson/" + id;
    }

    @PostMapping("/lesson/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String text, Principal principal) {
        AppUser teacher = currentUser.require(principal);
        Lesson lesson = lessons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!access.canAccessLesson(teacher, lesson)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (text != null && !text.isBlank()) {
            TeacherComment c = new TeacherComment();
            c.setLesson(lesson);
            c.setAuthor(teacher);
            c.setText(text);
            comments.save(c);
        }
        return "redirect:/teacher/lesson/" + id;
    }

    @PostMapping("/lesson/{id}/event")
    public String addEvent(@PathVariable Long id, @RequestParam String title,
                           @RequestParam EventType type, @RequestParam(required = false) String description,
                           Principal principal) {
        AppUser teacher = currentUser.require(principal);
        Lesson lesson = lessons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!access.canAccessLesson(teacher, lesson)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        SchoolEvent e = new SchoolEvent();
        e.setSchoolClass(lesson.getSchoolClass());
        e.setDate(lesson.getDate());
        e.setTitle(title);
        e.setType(type);
        e.setDescription(description);
        e.setCreatedBy(teacher);
        events.save(e);
        return "redirect:/teacher/lesson/" + id;
    }

    // Обзор для классного руководителя: вся группа за неделю. Доступ только своему руководителю.
    @GetMapping("/class/{id}")
    public String classOverview(@PathVariable Long id, @RequestParam(defaultValue = "0") int week,
                                Principal principal, Model model) {
        AppUser teacher = currentUser.require(principal);
        SchoolClass clazz = classes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!access.isSupervisorOf(teacher, clazz)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Вы не классный руководитель этой группы");
        }

        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusWeeks(week);
        LocalDate sunday = monday.plusDays(6);

        List<Lesson> weekLessons = lessons
                .findBySchoolClassAndDateBetweenOrderByDateAscStartTimeAsc(clazz, monday, sunday);
        List<WeekDay> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate d = monday.plusDays(i);
            days.add(new WeekDay(d, weekLessons.stream().filter(l -> l.getDate().equals(d)).toList(), 0));
        }

        List<AppUser> students = appUsers.findBySchoolClassOrderByLastNameAscFirstNameAsc(clazz);

        // Сводка оценок всех студентов группы за неделю (для классного руководителя).
        Map<Long, Map<Long, Integer>> gradeIndex = new HashMap<>();
        for (Grade g : grades.findByLesson_SchoolClassAndLesson_DateBetween(clazz, monday, sunday)) {
            gradeIndex.computeIfAbsent(g.getStudent().getId(), k -> new HashMap<>())
                    .put(g.getLesson().getId(), g.getValue());
        }
        List<ClassGradeRow> gradeRows = new ArrayList<>();
        for (AppUser s : students) {
            Map<Long, Integer> byLesson = gradeIndex.getOrDefault(s.getId(), Map.of());
            List<Integer> cells = new ArrayList<>();
            int sum = 0, count = 0;
            for (Lesson l : weekLessons) {
                Integer v = byLesson.get(l.getId());
                cells.add(v);
                if (v != null) {
                    sum += v;
                    count++;
                }
            }
            Double average = count == 0 ? null : Math.round((double) sum / count * 100) / 100.0;
            gradeRows.add(new ClassGradeRow(s, cells, average));
        }

        model.addAttribute("user", teacher);
        model.addAttribute("clazz", clazz);
        model.addAttribute("students", students);
        model.addAttribute("days", days);
        model.addAttribute("gradeLessons", weekLessons);
        model.addAttribute("gradeRows", gradeRows);
        model.addAttribute("week", week);
        model.addAttribute("monday", monday);
        model.addAttribute("sunday", sunday);
        return "teacher/class";
    }
}
