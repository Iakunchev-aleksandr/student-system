package com.school.repo;

import com.school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByOrderByOrderIndexAscNameAsc();

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
