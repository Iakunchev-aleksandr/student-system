package com.school.repo;

import com.school.model.Course;
import com.school.model.Holiday;
import com.school.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findAllByOrderByStartDateAsc();

    boolean existsByCourse(Course course);
    boolean existsBySchoolClass(SchoolClass schoolClass);
}
