package com.school.service;

import com.school.model.AppUser;
import com.school.model.Lesson;
import com.school.model.Role;
import com.school.model.ScheduleTemplate;
import com.school.model.SchoolClass;
import com.school.repo.ScheduleTemplateRepository;
import com.school.repo.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessService {

    // Режим просмотра группы во вкладке «Группы».
    public enum ViewMode {
        FULL, // все уроки группы (админ или классный руководитель этой группы)
        OWN   // только собственные уроки преподавателя
    }

    private final ScheduleTemplateRepository templates;
    private final SchoolClassRepository classes;

    public AccessService(ScheduleTemplateRepository templates, SchoolClassRepository classes) {
        this.templates = templates;
        this.classes = classes;
    }

    // Администратор имеет полный доступ ко всем урокам.
    // Предметник видит урок, если ведёт его сам.
    // Классный руководитель видит любой урок своей группы.
    public boolean canAccessLesson(AppUser user, Lesson lesson) {
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        if (lesson.getTeacher() != null && lesson.getTeacher().getId().equals(user.getId())) {
            return true;
        }
        return isSupervisorOf(user, lesson.getSchoolClass());
    }

    public boolean isSupervisorOf(AppUser teacher, SchoolClass schoolClass) {
        return schoolClass != null
                && schoolClass.getSupervisor() != null
                && schoolClass.getSupervisor().getId().equals(teacher.getId());
    }

    // Группы, видимые пользователю во вкладке «Группы».
    // Админ — все группы; преподаватель — где ведёт (из шаблонов) плюс где классный руководитель.
    // Порядок и уникальность сохраняются (LinkedHashMap по id).
    public List<SchoolClass> visibleClasses(AppUser user) {
        if (user.getRole() == Role.ADMIN) {
            return classes.findAllByOrderByStudyYearAscGroupCodeAsc();
        }
        Map<Long, SchoolClass> byId = new LinkedHashMap<>();
        for (ScheduleTemplate t : templates.findByTeacher(user)) {
            SchoolClass c = t.getSchoolClass();
            byId.putIfAbsent(c.getId(), c);
        }
        for (SchoolClass c : classes.findBySupervisor(user)) {
            byId.putIfAbsent(c.getId(), c);
        }
        return byId.values().stream()
                .sorted((a, b) -> {
                    int y = Integer.compare(a.getStudyYear(), b.getStudyYear());
                    return y != 0 ? y : a.getGroupCode().compareTo(b.getGroupCode());
                })
                .toList();
    }

    // Как показывать группу: полностью (админ / классный руководитель) или только свои уроки.
    public ViewMode viewMode(AppUser user, SchoolClass clazz) {
        if (user.getRole() == Role.ADMIN || isSupervisorOf(user, clazz)) {
            return ViewMode.FULL;
        }
        return ViewMode.OWN;
    }
}
