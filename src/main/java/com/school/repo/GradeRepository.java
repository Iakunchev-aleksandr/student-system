package com.school.repo;

import com.school.model.AppUser;
import com.school.model.Grade;
import com.school.model.Lesson;
import com.school.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByStudentAndLesson(AppUser student, Lesson lesson);
    List<Grade> findByLesson(Lesson lesson);
    List<Grade> findByStudent(AppUser student);
    List<Grade> findByStudentAndLesson_Date(AppUser student, LocalDate date);

    // Все оценки группы за период — для сводки классного руководителя.
    List<Grade> findByLesson_SchoolClassAndLesson_DateBetween(
            SchoolClass schoolClass, LocalDate from, LocalDate to);
}
