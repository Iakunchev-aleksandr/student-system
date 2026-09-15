package com.school.service;

import com.school.model.Course;
import com.school.model.Holiday;
import com.school.model.SchoolClass;
import com.school.repo.HolidayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Поиск праздника/каникул, действующих на дату для конкретной группы —
// для отображения подписи в расписании. Область по специфичности: группа > курс > глобально.
@Service
public class HolidayService {

    private final HolidayRepository holidays;

    public HolidayService(HolidayRepository holidays) {
        this.holidays = holidays;
    }

    public List<Holiday> all() {
        return holidays.findAllByOrderByStartDateAsc();
    }

    // Название праздника, покрывающего дату для группы, или null если праздника нет.
    // Для праздника без названия возвращает "" (пустую строку) — день выходной, но без имени.
    public String titleFor(SchoolClass clazz, LocalDate date, List<Holiday> all) {
        Holiday best = null;
        int bestScope = -1; // 0 = глобально, 1 = курс, 2 = группа
        for (Holiday h : all) {
            if (!h.covers(date) || !scopeMatches(h.getSchoolClass(), h.getCourse(), clazz)) {
                continue;
            }
            int scope = h.getSchoolClass() != null ? 2 : (h.getCourse() != null ? 1 : 0);
            if (scope > bestScope) {
                bestScope = scope;
                best = h;
            }
        }
        if (best == null) {
            return null;
        }
        return best.getTitle() == null ? "" : best.getTitle();
    }

    private boolean scopeMatches(SchoolClass scopeClass, Course scopeCourse, SchoolClass clazz) {
        if (clazz == null) {
            return scopeClass == null && scopeCourse == null; // без группы — только глобальные
        }
        if (scopeClass != null) {
            return scopeClass.getId().equals(clazz.getId());
        }
        if (scopeCourse != null) {
            return clazz.getCourse() != null && scopeCourse.getId().equals(clazz.getCourse().getId());
        }
        return true; // глобально
    }
}
