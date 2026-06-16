package com.school.repo;

import com.school.model.AppUser;
import com.school.model.Lesson;
import com.school.model.SchoolClass;
import com.school.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsByTeacher(AppUser teacher);
    boolean existsBySchoolClass(SchoolClass schoolClass);
    boolean existsBySubject(Subject subject);

    List<Lesson> findBySchoolClassAndDateOrderByStartTime(SchoolClass schoolClass, LocalDate date);

    List<Lesson> findBySchoolClassAndDateBetweenOrderByDateAscStartTimeAsc(
            SchoolClass schoolClass, LocalDate from, LocalDate to);

    List<Lesson> findByTeacherAndDateOrderByStartTime(AppUser teacher, LocalDate date);

    List<Lesson> findByTeacherAndDateBetweenOrderByDateAscStartTimeAsc(
            AppUser teacher, LocalDate from, LocalDate to);

    boolean existsBySchoolClassAndDateAndStartTimeAndSubjectId(
            SchoolClass schoolClass, LocalDate date, LocalTime startTime, Long subjectId);
}
