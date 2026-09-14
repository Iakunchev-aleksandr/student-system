package com.school.repo;

import com.school.model.Course;
import com.school.model.DayOverride;
import com.school.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayOverrideRepository extends JpaRepository<DayOverride, Long> {
    List<DayOverride> findAllByOrderByDateAsc();

    boolean existsByCourse(Course course);
    boolean existsBySchoolClass(SchoolClass schoolClass);
}
