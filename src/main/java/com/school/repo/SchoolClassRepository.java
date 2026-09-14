package com.school.repo;

import com.school.model.AppUser;
import com.school.model.Course;
import com.school.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findBySupervisor(AppUser supervisor);
    List<SchoolClass> findAllByOrderByStudyYearAscGroupCodeAsc();
    List<SchoolClass> findByCourseOrderByStudyYearAscGroupCodeAsc(Course course);

    boolean existsByStudyYearAndGroupCode(Integer studyYear, String groupCode);
    boolean existsByStudyYearAndGroupCodeAndIdNot(Integer studyYear, String groupCode, Long id);
    boolean existsByCourse(Course course);
}
