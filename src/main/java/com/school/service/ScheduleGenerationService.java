package com.school.service;

import com.school.model.Lesson;
import com.school.model.ScheduleTemplate;
import com.school.repo.LessonRepository;
import com.school.repo.ScheduleTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleGenerationService {

    private final ScheduleTemplateRepository templates;
    private final LessonRepository lessons;

    public ScheduleGenerationService(ScheduleTemplateRepository templates, LessonRepository lessons) {
        this.templates = templates;
        this.lessons = lessons;
    }

    // Разворачивает недельные шаблоны в конкретные уроки на отрезке [from, to].
    // Уже существующие уроки пропускаются — повторный запуск безопасен.
    @Transactional
    public int generate(LocalDate from, LocalDate to) {
        List<ScheduleTemplate> all = templates.findAll();
        int created = 0;
        for (LocalDate day = from; !day.isAfter(to); day = day.plusDays(1)) {
            for (ScheduleTemplate t : all) {
                if (t.getDayOfWeek() != day.getDayOfWeek()) {
                    continue;
                }
                boolean exists = lessons.existsBySchoolClassAndDateAndStartTimeAndSubjectId(
                        t.getSchoolClass(), day, t.getStartTime(), t.getSubject().getId());
                if (exists) {
                    continue;
                }
                Lesson l = new Lesson();
                l.setDate(day);
                l.setStartTime(t.getStartTime());
                l.setEndTime(t.getEndTime());
                l.setSchoolClass(t.getSchoolClass());
                l.setSubject(t.getSubject());
                l.setTeacher(t.getTeacher());
                l.setRoom(t.getRoom());
                lessons.save(l);
                created++;
            }
        }
        return created;
    }
}
