package com.school.web;

import com.school.model.*;
import com.school.repo.*;
import com.school.service.FileStorageService;
import com.school.service.ScheduleGenerationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppUserRepository users;
    private final SchoolClassRepository classes;
    private final SubjectRepository subjects;
    private final ScheduleTemplateRepository templates;
    private final LessonRepository lessons;
    private final GradeRepository grades;
    private final AttendanceRepository attendance;
    private final StudentNoteRepository notes;
    private final TeacherCommentRepository comments;
    private final SchoolEventRepository events;
    private final FileAttachmentRepository files;
    private final CourseRepository courses;
    private final HolidayRepository holidays;
    private final DayOverrideRepository dayOverrides;
    private final TermRepository terms;
    private final ScheduleGenerationService scheduleGen;
    private final FileStorageService storage;
    private final PasswordEncoder encoder;

    public AdminController(AppUserRepository users, SchoolClassRepository classes, SubjectRepository subjects,
                           ScheduleTemplateRepository templates, LessonRepository lessons, GradeRepository grades,
                           AttendanceRepository attendance, StudentNoteRepository notes,
                           TeacherCommentRepository comments, SchoolEventRepository events,
                           FileAttachmentRepository files, CourseRepository courses, HolidayRepository holidays,
                           DayOverrideRepository dayOverrides, TermRepository terms, ScheduleGenerationService scheduleGen,
                           FileStorageService storage, PasswordEncoder encoder) {
        this.users = users;
        this.classes = classes;
        this.subjects = subjects;
        this.templates = templates;
        this.lessons = lessons;
        this.grades = grades;
        this.attendance = attendance;
        this.notes = notes;
        this.comments = comments;
        this.events = events;
        this.files = files;
        this.courses = courses;
        this.holidays = holidays;
        this.dayOverrides = dayOverrides;
        this.terms = terms;
        this.scheduleGen = scheduleGen;
        this.storage = storage;
        this.encoder = encoder;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("userCount", users.count());
        model.addAttribute("courseCount", courses.count());
        model.addAttribute("classCount", classes.count());
        model.addAttribute("subjectCount", subjects.count());
        model.addAttribute("templateCount", templates.count());
        model.addAttribute("calendarCount", holidays.count() + dayOverrides.count());
        return "admin/dashboard";
    }

    // ===================== Пользователи =====================

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", users.findAll());
        model.addAttribute("classes", classes.findAllByOrderByStudyYearAscGroupCodeAsc());
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(@RequestParam String username, @RequestParam String password,
                             @RequestParam String lastName, @RequestParam String firstName,
                             @RequestParam Role role, @RequestParam(required = false) Long schoolClassId,
                             @RequestParam(required = false) String studentNumber,
                             @RequestParam(required = false) MultipartFile photo,
                             RedirectAttributes ra) {
        if (username == null || username.isBlank()) {
            ra.addFlashAttribute("error", "Логин обязателен");
            return "redirect:/admin/users";
        }
        if (users.existsByUsername(username)) {
            ra.addFlashAttribute("error", "Логин уже занят: " + username);
            return "redirect:/admin/users";
        }
        if (role == Role.STUDENT) {
            String err = validateStudentNumber(studentNumber, null);
            if (err != null) {
                ra.addFlashAttribute("error", err);
                return "redirect:/admin/users";
            }
        }
        AppUser u = new AppUser();
        u.setUsername(username.trim());
        u.setPassword(encoder.encode(password));
        u.setLastName(lastName);
        u.setFirstName(firstName);
        u.setRole(role);
        if (role == Role.STUDENT) {
            u.setStudentNumber(studentNumber.trim());
            if (schoolClassId != null) {
                u.setSchoolClass(classes.findById(schoolClassId).orElse(null));
            }
        }
        String stored = storePhoto(photo);
        if (stored != null) {
            u.setPhotoName(stored);
        }
        users.save(u);
        ra.addFlashAttribute("message", "Пользователь создан");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        AppUser u = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("editUser", u);
        model.addAttribute("classes", classes.findAllByOrderByStudyYearAscGroupCodeAsc());
        model.addAttribute("roles", Role.values());
        return "admin/user-edit";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @RequestParam String username,
                             @RequestParam(required = false) String password,
                             @RequestParam String lastName, @RequestParam String firstName,
                             @RequestParam Role role, @RequestParam(required = false) Long schoolClassId,
                             @RequestParam(required = false) String studentNumber,
                             @RequestParam(required = false) MultipartFile photo,
                             RedirectAttributes ra) {
        AppUser u = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (users.existsByUsernameAndIdNot(username, id)) {
            ra.addFlashAttribute("error", "Логин уже занят: " + username);
            return "redirect:/admin/users/" + id + "/edit";
        }
        if (role == Role.STUDENT) {
            String err = validateStudentNumber(studentNumber, id);
            if (err != null) {
                ra.addFlashAttribute("error", err);
                return "redirect:/admin/users/" + id + "/edit";
            }
        }
        u.setUsername(username.trim());
        u.setLastName(lastName);
        u.setFirstName(firstName);
        u.setRole(role);
        if (password != null && !password.isBlank()) {
            u.setPassword(encoder.encode(password));
        }
        if (role == Role.STUDENT) {
            u.setStudentNumber(studentNumber.trim());
            u.setSchoolClass(schoolClassId != null ? classes.findById(schoolClassId).orElse(null) : null);
        } else {
            u.setStudentNumber(null);
            u.setSchoolClass(null);
        }
        String stored = storePhoto(photo);
        if (stored != null) {
            u.setPhotoName(stored);
        }
        users.save(u);
        ra.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        AppUser u = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (u.getRole() == Role.STUDENT) {
            // У студента удаляем связанные оценки, посещаемость и заметки, затем самого пользователя.
            grades.deleteAll(grades.findByStudent(u));
            attendance.deleteAll(attendance.findByStudent(u));
            notes.deleteAll(notes.findByStudent(u));
            users.delete(u);
            ra.addFlashAttribute("message", "Студент удалён");
        } else {
            // Преподавателя/админа нельзя удалить, пока на него ссылаются уроки, шаблоны, события или комментарии.
            if (lessons.existsByTeacher(u) || templates.existsByTeacher(u)
                    || events.existsByCreatedBy(u) || comments.existsByAuthor(u)
                    || files.existsByUploadedBy(u)) {
                ra.addFlashAttribute("error",
                        "Нельзя удалить: на пользователя ссылаются уроки, шаблоны, события или комментарии");
                return "redirect:/admin/users";
            }
            // Снимаем с роли классного руководителя, если назначен.
            for (SchoolClass c : classes.findBySupervisor(u)) {
                c.setSupervisor(null);
                classes.save(c);
            }
            users.delete(u);
            ra.addFlashAttribute("message", "Пользователь удалён");
        }
        return "redirect:/admin/users";
    }

    // ===================== Группы =====================

    @GetMapping("/classes")
    public String classes(Model model) {
        model.addAttribute("classes", classes.findAllByOrderByStudyYearAscGroupCodeAsc());
        model.addAttribute("teachers", users.findByRoleOrderByLastNameAscFirstNameAsc(Role.TEACHER));
        model.addAttribute("courses", courses.findAllByOrderByOrderIndexAscNameAsc());
        return "admin/classes";
    }

    @PostMapping("/classes")
    public String createClass(@RequestParam Integer studyYear, @RequestParam String groupCode,
                              @RequestParam Long courseId,
                              @RequestParam(required = false) Long supervisorId, RedirectAttributes ra) {
        if (classes.existsByStudyYearAndGroupCode(studyYear, groupCode)) {
            ra.addFlashAttribute("error", "Такая группа уже существует");
            return "redirect:/admin/classes";
        }
        Course course = courses.findById(courseId).orElse(null);
        if (course == null) {
            ra.addFlashAttribute("error", "Выберите курс");
            return "redirect:/admin/classes";
        }
        SchoolClass c = new SchoolClass();
        c.setStudyYear(studyYear);
        c.setGroupCode(groupCode);
        c.setCourse(course);
        if (supervisorId != null) {
            c.setSupervisor(users.findById(supervisorId).orElse(null));
        }
        classes.save(c);
        ra.addFlashAttribute("message", "Группа создана");
        return "redirect:/admin/classes";
    }

    @GetMapping("/classes/{id}/edit")
    public String editClassForm(@PathVariable Long id, Model model) {
        SchoolClass c = classes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("editClass", c);
        model.addAttribute("teachers", users.findByRoleOrderByLastNameAscFirstNameAsc(Role.TEACHER));
        model.addAttribute("courses", courses.findAllByOrderByOrderIndexAscNameAsc());
        return "admin/class-edit";
    }

    @PostMapping("/classes/{id}")
    public String updateClass(@PathVariable Long id, @RequestParam Integer studyYear,
                              @RequestParam String groupCode, @RequestParam Long courseId,
                              @RequestParam(required = false) Long supervisorId,
                              RedirectAttributes ra) {
        SchoolClass c = classes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (classes.existsByStudyYearAndGroupCodeAndIdNot(studyYear, groupCode, id)) {
            ra.addFlashAttribute("error", "Такая группа уже существует");
            return "redirect:/admin/classes/" + id + "/edit";
        }
        Course course = courses.findById(courseId).orElse(null);
        if (course == null) {
            ra.addFlashAttribute("error", "Выберите курс");
            return "redirect:/admin/classes/" + id + "/edit";
        }
        c.setStudyYear(studyYear);
        c.setGroupCode(groupCode);
        c.setCourse(course);
        c.setSupervisor(supervisorId != null ? users.findById(supervisorId).orElse(null) : null);
        classes.save(c);
        ra.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/admin/classes";
    }

    @PostMapping("/classes/{id}/supervisor")
    public String setSupervisor(@PathVariable Long id, @RequestParam(required = false) Long supervisorId) {
        SchoolClass c = classes.findById(id).orElseThrow();
        c.setSupervisor(supervisorId == null ? null : users.findById(supervisorId).orElse(null));
        classes.save(c);
        return "redirect:/admin/classes";
    }

    @PostMapping("/classes/{id}/delete")
    public String deleteClass(@PathVariable Long id, RedirectAttributes ra) {
        SchoolClass c = classes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (users.existsBySchoolClass(c) || lessons.existsBySchoolClass(c)
                || templates.existsBySchoolClass(c) || events.existsBySchoolClass(c)) {
            ra.addFlashAttribute("error",
                    "Нельзя удалить: к группе привязаны студенты, уроки, шаблоны или события");
            return "redirect:/admin/classes";
        }
        classes.delete(c);
        ra.addFlashAttribute("message", "Группа удалена");
        return "redirect:/admin/classes";
    }

    // ===================== Курсы =====================

    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courses", courses.findAllByOrderByOrderIndexAscNameAsc());
        return "admin/courses";
    }

    @PostMapping("/courses")
    public String createCourse(@RequestParam String name, @RequestParam(defaultValue = "0") int orderIndex,
                               RedirectAttributes ra) {
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "Название обязательно");
            return "redirect:/admin/courses";
        }
        if (courses.existsByName(name.trim())) {
            ra.addFlashAttribute("error", "Такой курс уже есть");
            return "redirect:/admin/courses";
        }
        Course c = new Course();
        c.setName(name.trim());
        c.setOrderIndex(orderIndex);
        courses.save(c);
        ra.addFlashAttribute("message", "Курс добавлен");
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/{id}")
    public String updateCourse(@PathVariable Long id, @RequestParam String name,
                               @RequestParam(defaultValue = "0") int orderIndex, RedirectAttributes ra) {
        Course c = courses.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "Название обязательно");
            return "redirect:/admin/courses";
        }
        if (courses.existsByNameAndIdNot(name.trim(), id)) {
            ra.addFlashAttribute("error", "Такой курс уже есть");
            return "redirect:/admin/courses";
        }
        c.setName(name.trim());
        c.setOrderIndex(orderIndex);
        courses.save(c);
        ra.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes ra) {
        Course c = courses.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (classes.existsByCourse(c) || holidays.existsByCourse(c) || dayOverrides.existsByCourse(c)) {
            ra.addFlashAttribute("error", "Нельзя удалить: к курсу привязаны группы, каникулы или замены");
            return "redirect:/admin/courses";
        }
        courses.delete(c);
        ra.addFlashAttribute("message", "Курс удалён");
        return "redirect:/admin/courses";
    }

    // ===================== Семестры (学期) =====================

    @GetMapping("/terms")
    public String termsPage(Model model) {
        model.addAttribute("terms", terms.findAllByOrderByStartDateAsc());
        return "admin/terms";
    }

    @PostMapping("/terms")
    public String createTerm(@RequestParam String name,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                             RedirectAttributes ra) {
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "Название обязательно");
            return "redirect:/admin/terms";
        }
        if (endDate.isBefore(startDate)) {
            ra.addFlashAttribute("error", "Дата окончания раньше начала");
            return "redirect:/admin/terms";
        }
        Term t = new Term();
        t.setName(name.trim());
        t.setStartDate(startDate);
        t.setEndDate(endDate);
        terms.save(t);
        ra.addFlashAttribute("message", "Семестр добавлен");
        return "redirect:/admin/terms";
    }

    @PostMapping("/terms/{id}")
    public String updateTerm(@PathVariable Long id, @RequestParam String name,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                             RedirectAttributes ra) {
        Term t = terms.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (name == null || name.isBlank() || endDate.isBefore(startDate)) {
            ra.addFlashAttribute("error", "Проверьте название и даты");
            return "redirect:/admin/terms";
        }
        t.setName(name.trim());
        t.setStartDate(startDate);
        t.setEndDate(endDate);
        terms.save(t);
        ra.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/admin/terms";
    }

    @PostMapping("/terms/{id}/delete")
    public String deleteTerm(@PathVariable Long id, RedirectAttributes ra) {
        Term t = terms.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (templates.existsByTerm(t)) {
            ra.addFlashAttribute("error", "Нельзя удалить: к семестру привязаны слоты расписания");
            return "redirect:/admin/terms";
        }
        terms.delete(t);
        ra.addFlashAttribute("message", "Семестр удалён");
        return "redirect:/admin/terms";
    }

    // ===================== Каникулы и замены дней =====================

    @GetMapping("/calendar")
    public String calendar(Model model) {
        model.addAttribute("holidays", holidays.findAllByOrderByStartDateAsc());
        model.addAttribute("dayOverrides", dayOverrides.findAllByOrderByDateAsc());
        model.addAttribute("courses", courses.findAllByOrderByOrderIndexAscNameAsc());
        model.addAttribute("classes", classes.findAllByOrderByStudyYearAscGroupCodeAsc());
        model.addAttribute("days", DayOfWeek.values());
        return "admin/calendar";
    }

    @PostMapping("/calendar/holidays")
    public String createHoliday(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                @RequestParam(required = false) String title,
                                @RequestParam(required = false) Long courseId,
                                @RequestParam(required = false) Long schoolClassId, RedirectAttributes ra) {
        if (endDate.isBefore(startDate)) {
            ra.addFlashAttribute("error", "Дата окончания раньше начала");
            return "redirect:/admin/calendar";
        }
        Holiday h = new Holiday();
        h.setStartDate(startDate);
        h.setEndDate(endDate);
        h.setTitle(title);
        applyScope(h::setCourse, h::setSchoolClass, courseId, schoolClassId);
        holidays.save(h);
        ra.addFlashAttribute("message", "Каникулы добавлены");
        return "redirect:/admin/calendar";
    }

    @PostMapping("/calendar/holidays/{id}/delete")
    public String deleteHoliday(@PathVariable Long id, RedirectAttributes ra) {
        holidays.deleteById(id);
        ra.addFlashAttribute("message", "Каникулы удалены");
        return "redirect:/admin/calendar";
    }

    @PostMapping("/calendar/day-overrides")
    public String createDayOverride(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    @RequestParam DayOfWeek substituteDayOfWeek,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) Long courseId,
                                    @RequestParam(required = false) Long schoolClassId, RedirectAttributes ra) {
        DayOverride o = new DayOverride();
        o.setDate(date);
        o.setSubstituteDayOfWeek(substituteDayOfWeek);
        o.setTitle(title);
        applyScope(o::setCourse, o::setSchoolClass, courseId, schoolClassId);
        dayOverrides.save(o);
        ra.addFlashAttribute("message", "Замена дня добавлена");
        return "redirect:/admin/calendar";
    }

    @PostMapping("/calendar/day-overrides/{id}/delete")
    public String deleteDayOverride(@PathVariable Long id, RedirectAttributes ra) {
        dayOverrides.deleteById(id);
        ra.addFlashAttribute("message", "Замена дня удалена");
        return "redirect:/admin/calendar";
    }

    // Устанавливает область: группа важнее курса; если задана группа — курс игнорируется.
    private void applyScope(java.util.function.Consumer<Course> setCourse,
                            java.util.function.Consumer<SchoolClass> setClass,
                            Long courseId, Long schoolClassId) {
        if (schoolClassId != null) {
            setClass.accept(classes.findById(schoolClassId).orElse(null));
            setCourse.accept(null);
        } else if (courseId != null) {
            setCourse.accept(courses.findById(courseId).orElse(null));
            setClass.accept(null);
        }
    }

    // ===================== Предметы =====================

    @GetMapping("/subjects")
    public String subjects(Model model) {
        model.addAttribute("subjects", subjects.findAllByOrderByName());
        return "admin/subjects";
    }

    @PostMapping("/subjects")
    public String createSubject(@RequestParam String name, RedirectAttributes ra) {
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "Название обязательно");
            return "redirect:/admin/subjects";
        }
        if (subjects.existsByName(name)) {
            ra.addFlashAttribute("error", "Такой предмет уже есть");
            return "redirect:/admin/subjects";
        }
        Subject s = new Subject();
        s.setName(name.trim());
        subjects.save(s);
        ra.addFlashAttribute("message", "Предмет добавлен");
        return "redirect:/admin/subjects";
    }

    @PostMapping("/subjects/{id}")
    public String updateSubject(@PathVariable Long id, @RequestParam String name, RedirectAttributes ra) {
        Subject s = subjects.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "Название обязательно");
            return "redirect:/admin/subjects";
        }
        if (subjects.existsByNameAndIdNot(name, id)) {
            ra.addFlashAttribute("error", "Такой предмет уже есть");
            return "redirect:/admin/subjects";
        }
        s.setName(name.trim());
        subjects.save(s);
        ra.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/admin/subjects";
    }

    @PostMapping("/subjects/{id}/delete")
    public String deleteSubject(@PathVariable Long id, RedirectAttributes ra) {
        Subject s = subjects.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (lessons.existsBySubject(s) || templates.existsBySubject(s)) {
            ra.addFlashAttribute("error", "Нельзя удалить: предмет используется в расписании или уроках");
            return "redirect:/admin/subjects";
        }
        subjects.delete(s);
        ra.addFlashAttribute("message", "Предмет удалён");
        return "redirect:/admin/subjects";
    }

    // ===================== Шаблон расписания =====================

    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("templates", templates.findAllByOrderByDayOfWeekAscStartTimeAsc());
        model.addAttribute("classes", classes.findAllByOrderByStudyYearAscGroupCodeAsc());
        model.addAttribute("subjects", subjects.findAllByOrderByName());
        model.addAttribute("teachers", users.findByRoleOrderByLastNameAscFirstNameAsc(Role.TEACHER));
        model.addAttribute("terms", terms.findAllByOrderByStartDateAsc());
        model.addAttribute("days", DayOfWeek.values());
        return "admin/schedule";
    }

    @PostMapping("/schedule")
    public String createTemplate(@RequestParam Long schoolClassId, @RequestParam Long subjectId,
                                 @RequestParam Long teacherId, @RequestParam(required = false) Long termId,
                                 @RequestParam DayOfWeek dayOfWeek,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                 @RequestParam(required = false) String room, RedirectAttributes ra) {
        if (!endTime.isAfter(startTime)) {
            ra.addFlashAttribute("error", "Время окончания должно быть позже начала");
            return "redirect:/admin/schedule";
        }
        ScheduleTemplate t = new ScheduleTemplate();
        t.setSchoolClass(classes.findById(schoolClassId).orElseThrow());
        t.setSubject(subjects.findById(subjectId).orElseThrow());
        t.setTeacher(users.findById(teacherId).orElseThrow());
        t.setTerm(termId != null ? terms.findById(termId).orElse(null) : null);
        t.setDayOfWeek(dayOfWeek);
        t.setStartTime(startTime);
        t.setEndTime(endTime);
        t.setRoom(room);
        templates.save(t);
        ra.addFlashAttribute("message", "Слот добавлен");
        return "redirect:/admin/schedule";
    }

    @GetMapping("/schedule/{id}/edit")
    public String editTemplateForm(@PathVariable Long id, Model model) {
        ScheduleTemplate t = templates.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("editTemplate", t);
        model.addAttribute("classes", classes.findAllByOrderByStudyYearAscGroupCodeAsc());
        model.addAttribute("subjects", subjects.findAllByOrderByName());
        model.addAttribute("teachers", users.findByRoleOrderByLastNameAscFirstNameAsc(Role.TEACHER));
        model.addAttribute("terms", terms.findAllByOrderByStartDateAsc());
        model.addAttribute("days", DayOfWeek.values());
        return "admin/schedule-edit";
    }

    @PostMapping("/schedule/{id}")
    public String updateTemplate(@PathVariable Long id, @RequestParam Long schoolClassId,
                                 @RequestParam Long subjectId, @RequestParam Long teacherId,
                                 @RequestParam(required = false) Long termId,
                                 @RequestParam DayOfWeek dayOfWeek,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                 @RequestParam(required = false) String room, RedirectAttributes ra) {
        ScheduleTemplate t = templates.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!endTime.isAfter(startTime)) {
            ra.addFlashAttribute("error", "Время окончания должно быть позже начала");
            return "redirect:/admin/schedule/" + id + "/edit";
        }
        t.setSchoolClass(classes.findById(schoolClassId).orElseThrow());
        t.setSubject(subjects.findById(subjectId).orElseThrow());
        t.setTeacher(users.findById(teacherId).orElseThrow());
        t.setTerm(termId != null ? terms.findById(termId).orElse(null) : null);
        t.setDayOfWeek(dayOfWeek);
        t.setStartTime(startTime);
        t.setEndTime(endTime);
        t.setRoom(room);
        templates.save(t);
        ra.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/admin/schedule";
    }

    @PostMapping("/schedule/{id}/delete")
    public String deleteTemplate(@PathVariable Long id, RedirectAttributes ra) {
        templates.deleteById(id);
        ra.addFlashAttribute("message", "Слот удалён");
        return "redirect:/admin/schedule";
    }

    // ===================== Генерация уроков =====================

    @GetMapping("/generate")
    public String generateForm(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("from", today);
        model.addAttribute("to", today.plusDays(13));
        return "admin/generate";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                           Model model) {
        int created = 0;
        if (!to.isBefore(from)) {
            created = scheduleGen.generate(from, to);
        }
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("created", created);
        return "admin/generate";
    }

    // ===================== Журнал (заменён вкладкой «Группы») =====================

    @GetMapping("/journal")
    public String journal() {
        return "redirect:/groups";
    }

    // ===================== Вспомогательное =====================

    // Возвращает текст ошибки или null, если номер корректен.
    private String validateStudentNumber(String value, Long currentUserId) {
        if (value == null || value.isBlank()) {
            return "Укажите 6-значный номер студента";
        }
        String v = value.trim();
        if (!v.matches("\\d{6}")) {
            return "Номер студента должен состоять ровно из 6 цифр";
        }
        boolean taken = currentUserId == null
                ? users.existsByStudentNumber(v)
                : users.existsByStudentNumberAndIdNot(v, currentUserId);
        if (taken) {
            return "Номер студента уже занят: " + v;
        }
        return null;
    }

    private String storePhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return null;
        }
        return storage.store(photo);
    }
}
