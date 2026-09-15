package com.school.web;

import com.school.model.*;
import com.school.repo.AppUserRepository;
import com.school.repo.AttendanceRepository;
import com.school.repo.CourseRepository;
import com.school.repo.LessonRepository;
import com.school.repo.SchoolClassRepository;
import com.school.service.AccessService;
import com.school.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Единая вкладка «Группы»: курс → группа → неделя. Заменяет прежние
// «Моё расписание» (учитель) и «Журнал» (админ). Видимость зависит от роли.
@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final CurrentUserService currentUser;
    private final AccessService access;
    private final CourseRepository courses;
    private final SchoolClassRepository classes;
    private final LessonRepository lessons;
    private final AttendanceRepository attendance;
    private final AppUserRepository appUsers;

    public GroupsController(CurrentUserService currentUser, AccessService access, CourseRepository courses,
                            SchoolClassRepository classes, LessonRepository lessons, AttendanceRepository attendance,
                            AppUserRepository appUsers) {
        this.currentUser = currentUser;
        this.access = access;
        this.courses = courses;
        this.classes = classes;
        this.lessons = lessons;
        this.attendance = attendance;
        this.appUsers = appUsers;
    }

    @GetMapping
    public String groups(@RequestParam(required = false) Long courseId,
                         @RequestParam(required = false) Long classId,
                         @RequestParam(defaultValue = "0") int week,
                         Principal principal, Model model) {
        AppUser user = currentUser.require(principal);

        List<SchoolClass> visible = access.visibleClasses(user);

        // Курсы для верхних вкладок: админ — все; преподаватель — только те, где есть видимые группы.
        List<Course> courseTabs = courseTabs(user, visible);

        // Выбранный курс (по параметру либо первый доступный).
        Course selectedCourse = pick(courseTabs, courseId);

        // Группы выбранного курса (из числа видимых).
        List<SchoolClass> groupTabs = new ArrayList<>();
        if (selectedCourse != null) {
            for (SchoolClass c : visible) {
                if (c.getCourse() != null && c.getCourse().getId().equals(selectedCourse.getId())) {
                    groupTabs.add(c);
                }
            }
        }

        // Выбранная группа (по параметру, если она в списке; иначе первая).
        SchoolClass selectedClass = null;
        if (classId != null) {
            selectedClass = groupTabs.stream().filter(c -> c.getId().equals(classId)).findFirst().orElse(null);
        }
        if (selectedClass == null && !groupTabs.isEmpty()) {
            selectedClass = groupTabs.get(0);
        }

        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusWeeks(week);
        LocalDate sunday = monday.plusDays(6);

        AccessService.ViewMode viewMode = null;
        List<WeekDay> days = new ArrayList<>();
        List<Lesson> weekLessons = new ArrayList<>();
        List<AppUser> students = List.of();
        List<ClassAttendanceRow> attendanceRows = new ArrayList<>();

        if (selectedClass != null) {
            viewMode = access.viewMode(user, selectedClass);
            weekLessons = lessons.findBySchoolClassAndDateBetweenOrderByDateAscStartTimeAsc(selectedClass, monday, sunday);
            if (viewMode == AccessService.ViewMode.OWN) {
                weekLessons = weekLessons.stream()
                        .filter(l -> l.getTeacher() != null && l.getTeacher().getId().equals(user.getId()))
                        .toList();
            }
            for (int i = 0; i < 7; i++) {
                LocalDate d = monday.plusDays(i);
                List<Lesson> ll = new ArrayList<>();
                for (Lesson l : weekLessons) {
                    if (l.getDate().equals(d)) {
                        ll.add(l);
                    }
                }
                days.add(new WeekDay(d, ll, 0));
            }

            // Сводка посещаемости за неделю — только в полном режиме (админ / классный руководитель).
            if (viewMode == AccessService.ViewMode.FULL) {
                students = appUsers.findBySchoolClassOrderByLastNameAscFirstNameAsc(selectedClass);
                Map<Long, Map<Long, AttendanceStatus>> attIndex = new LinkedHashMap<>();
                for (Attendance a : attendance.findByLesson_SchoolClassAndLesson_DateBetween(selectedClass, monday, sunday)) {
                    attIndex.computeIfAbsent(a.getStudent().getId(), k -> new LinkedHashMap<>())
                            .put(a.getLesson().getId(), a.getStatus());
                }
                for (AppUser s : students) {
                    Map<Long, AttendanceStatus> byLesson = attIndex.getOrDefault(s.getId(), Map.of());
                    List<AttendanceStatus> cells = new ArrayList<>();
                    int present = 0;
                    for (Lesson l : weekLessons) {
                        AttendanceStatus st = byLesson.get(l.getId());
                        cells.add(st);
                        if (st == AttendanceStatus.PRESENT || st == AttendanceStatus.LATE) {
                            present++;
                        }
                    }
                    attendanceRows.add(new ClassAttendanceRow(s, cells, present, weekLessons.size()));
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("courseTabs", courseTabs);
        model.addAttribute("selectedCourse", selectedCourse);
        model.addAttribute("groupTabs", groupTabs);
        model.addAttribute("selectedClass", selectedClass);
        model.addAttribute("days", days);
        model.addAttribute("full", viewMode == AccessService.ViewMode.FULL);
        model.addAttribute("students", students);
        model.addAttribute("summaryLessons", weekLessons);
        model.addAttribute("attendanceRows", attendanceRows);
        model.addAttribute("week", week);
        model.addAttribute("monday", monday);
        model.addAttribute("sunday", sunday);
        return "groups/index";
    }

    // Верхние вкладки курсов: админ видит все курсы, преподаватель — только курсы своих видимых групп.
    private List<Course> courseTabs(AppUser user, List<SchoolClass> visible) {
        if (user.getRole() == Role.ADMIN) {
            return courses.findAllByOrderByOrderIndexAscNameAsc();
        }
        Map<Long, Course> byId = new LinkedHashMap<>();
        for (SchoolClass c : visible) {
            if (c.getCourse() != null) {
                byId.putIfAbsent(c.getCourse().getId(), c.getCourse());
            }
        }
        return byId.values().stream()
                .sorted((a, b) -> {
                    int o = Integer.compare(a.getOrderIndex(), b.getOrderIndex());
                    return o != 0 ? o : a.getName().compareTo(b.getName());
                })
                .toList();
    }

    private Course pick(List<Course> list, Long id) {
        if (id != null) {
            for (Course c : list) {
                if (c.getId().equals(id)) {
                    return c;
                }
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }
}
