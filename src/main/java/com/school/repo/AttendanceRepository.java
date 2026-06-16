package com.school.repo;

import com.school.model.AppUser;
import com.school.model.Attendance;
import com.school.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentAndLesson(AppUser student, Lesson lesson);
    List<Attendance> findByLesson(Lesson lesson);
    List<Attendance> findByStudent(AppUser student);
}
