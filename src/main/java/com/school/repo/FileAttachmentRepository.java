package com.school.repo;

import com.school.model.AppUser;
import com.school.model.FileAttachment;
import com.school.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    List<FileAttachment> findByLessonOrderByUploadedAt(Lesson lesson);
    boolean existsByUploadedBy(AppUser uploadedBy);
}
