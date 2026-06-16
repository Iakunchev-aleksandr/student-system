package com.school.repo;

import com.school.model.AppUser;
import com.school.model.Lesson;
import com.school.model.TeacherComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherCommentRepository extends JpaRepository<TeacherComment, Long> {
    List<TeacherComment> findByLessonOrderByCreatedAt(Lesson lesson);
    boolean existsByAuthor(AppUser author);
}
