package com.school.repo;

import com.school.model.AppUser;
import com.school.model.SchoolClass;
import com.school.model.StudentNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentNoteRepository extends JpaRepository<StudentNote, Long> {
    Optional<StudentNote> findByStudentAndDate(AppUser student, LocalDate date);
    List<StudentNote> findByStudent_SchoolClassAndDateAndForTeacherTrue(SchoolClass schoolClass, LocalDate date);
    List<StudentNote> findByStudent(AppUser student);
}
