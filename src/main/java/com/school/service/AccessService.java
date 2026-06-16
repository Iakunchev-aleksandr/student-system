package com.school.service;

import com.school.model.AppUser;
import com.school.model.Lesson;
import com.school.model.Role;
import com.school.model.SchoolClass;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

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
}
