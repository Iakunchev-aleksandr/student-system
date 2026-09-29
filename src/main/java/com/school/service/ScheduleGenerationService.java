package com.school.service;

import com.school.model.DayOverride;
import com.school.model.Holiday;
import com.school.model.Lesson;
import com.school.model.ScheduleTemplate;
import com.school.model.SchoolClass;
import com.school.repo.AttendanceRepository;
import com.school.repo.DayOverrideRepository;
import com.school.repo.GradeRepository;
import com.school.repo.HolidayRepository;
import com.school.repo.LessonRepository;
import com.school.repo.ScheduleTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleGenerationService {

    private final ScheduleTemplateRepository templates;
    private final LessonRepository lessons;
    private final HolidayRepository holidays;
    private final DayOverrideRepository dayOverrides;
    private final GradeRepository grades;
    private final AttendanceRepository attendance;

    public ScheduleGenerationService(ScheduleTemplateRepository templates, LessonRepository lessons,
                                     HolidayRepository holidays, DayOverrideRepository dayOverrides,
                                     GradeRepository grades, AttendanceRepository attendance) {
        this.templates = templates;
        this.lessons = lessons;
        this.holidays = holidays;
        this.dayOverrides = dayOverrides;
        this.grades = grades;
        this.attendance = attendance;
    }

    // Итог пересборки расписания: добавлено новых, удалено лишних, оставлено с оценками.
    public record ResyncResult(int added, int removed, int kept) {}

    // Пересобирает уроки в диапазоне под текущие шаблоны/семестры/каникулы/замены:
    // добавляет недостающие и удаляет лишние — но уроки с оценками/посещаемостью не трогает.
    @Transactional
    public ResyncResult resync(LocalDate from, LocalDate to) {
        int added = generate(from, to);
        List<ScheduleTemplate> allTemplates = templates.findAll();
        List<Holiday> allHolidays = holidays.findAll();
        List<DayOverride> allOverrides = dayOverrides.findAll();
        int removed = 0, kept = 0;
        for (Lesson l : lessons.findByDateBetween(from, to)) {
            if (isExpected(l, allTemplates, allHolidays, allOverrides)) {
                continue;
            }
            if (grades.existsByLesson(l) || attendance.existsByLesson(l)) {
                kept++;
            } else {
                lessons.delete(l);
                removed++;
            }
        }
        return new ResyncResult(added, removed, kept);
    }

    // Соответствует ли урок какому-либо действующему шаблону на его дату.
    private boolean isExpected(Lesson l, List<ScheduleTemplate> allTemplates,
                               List<Holiday> allHolidays, List<DayOverride> allOverrides) {
        SchoolClass clazz = l.getSchoolClass();
        LocalDate date = l.getDate();
        if (isHoliday(clazz, date, allHolidays)) {
            return false;
        }
        DayOfWeek eff = effectiveDayOfWeek(clazz, date, allOverrides);
        for (ScheduleTemplate t : allTemplates) {
            if (!t.getSchoolClass().getId().equals(clazz.getId())) {
                continue;
            }
            if (t.getTerm() != null && !t.getTerm().covers(date)) {
                continue;
            }
            if (t.getDayOfWeek() != eff) {
                continue;
            }
            if (t.getStartTime().equals(l.getStartTime())
                    && t.getSubject().getId().equals(l.getSubject().getId())) {
                return true;
            }
        }
        return false;
    }

    // Разворачивает недельные шаблоны в конкретные уроки на отрезке [from, to].
    // Учитывает каникулы (в эти даты урок не создаётся) и замену дня (振替 —
    // в дату действует расписание другого дня недели). По специфичности:
    // группа > курс > глобально.
    // Уже существующие уроки пропускаются, ничего не удаляется — повторный запуск безопасен.
    @Transactional
    public int generate(LocalDate from, LocalDate to) {
        List<ScheduleTemplate> allTemplates = templates.findAll();
        List<Holiday> allHolidays = holidays.findAll();
        List<DayOverride> allOverrides = dayOverrides.findAll();
        int created = 0;
        for (LocalDate day = from; !day.isAfter(to); day = day.plusDays(1)) {
            for (ScheduleTemplate t : allTemplates) {
                SchoolClass clazz = t.getSchoolClass();
                // Слот с привязкой к семестру действует только внутри его периода.
                if (t.getTerm() != null && !t.getTerm().covers(day)) {
                    continue;
                }
                if (isHoliday(clazz, day, allHolidays)) {
                    continue;
                }
                if (t.getDayOfWeek() != effectiveDayOfWeek(clazz, day, allOverrides)) {
                    continue;
                }
                boolean exists = lessons.existsBySchoolClassAndDateAndStartTimeAndSubjectId(
                        clazz, day, t.getStartTime(), t.getSubject().getId());
                if (exists) {
                    continue;
                }
                Lesson l = new Lesson();
                l.setDate(day);
                l.setStartTime(t.getStartTime());
                l.setEndTime(t.getEndTime());
                l.setSchoolClass(clazz);
                l.setSubject(t.getSubject());
                l.setTeacher(t.getTeacher());
                l.setRoom(t.getRoom());
                lessons.save(l);
                created++;
            }
        }
        return created;
    }

    // Есть ли каникулы, покрывающие дату для этой группы (по группе / курсу / глобально).
    private boolean isHoliday(SchoolClass clazz, LocalDate date, List<Holiday> allHolidays) {
        for (Holiday h : allHolidays) {
            if (h.covers(date) && scopeMatches(h.getSchoolClass(), h.getCourse(), clazz)) {
                return true;
            }
        }
        return false;
    }

    // Эффективный день недели с учётом замены. Самая специфичная замена (группа > курс > глобально) побеждает.
    private DayOfWeek effectiveDayOfWeek(SchoolClass clazz, LocalDate date, List<DayOverride> allOverrides) {
        DayOverride best = null;
        int bestScope = -1; // 0 = глобально, 1 = курс, 2 = группа
        for (DayOverride o : allOverrides) {
            if (!o.getDate().equals(date) || !scopeMatches(o.getSchoolClass(), o.getCourse(), clazz)) {
                continue;
            }
            int scope = o.getSchoolClass() != null ? 2 : (o.getCourse() != null ? 1 : 0);
            if (scope > bestScope) {
                bestScope = scope;
                best = o;
            }
        }
        return best != null ? best.getSubstituteDayOfWeek() : date.getDayOfWeek();
    }

    // Область (группа/курс/глобально) распространяется на данную группу?
    private boolean scopeMatches(SchoolClass scopeClass, com.school.model.Course scopeCourse, SchoolClass clazz) {
        if (scopeClass != null) {
            return scopeClass.getId().equals(clazz.getId());
        }
        if (scopeCourse != null) {
            return clazz.getCourse() != null && scopeCourse.getId().equals(clazz.getCourse().getId());
        }
        return true; // глобально
    }
}
