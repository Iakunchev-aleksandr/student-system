package com.school.service;

import com.school.model.DayOverride;
import com.school.model.Holiday;
import com.school.model.Lesson;
import com.school.model.ScheduleTemplate;
import com.school.model.SchoolClass;
import com.school.repo.DayOverrideRepository;
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

    public ScheduleGenerationService(ScheduleTemplateRepository templates, LessonRepository lessons,
                                     HolidayRepository holidays, DayOverrideRepository dayOverrides) {
        this.templates = templates;
        this.lessons = lessons;
        this.holidays = holidays;
        this.dayOverrides = dayOverrides;
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
