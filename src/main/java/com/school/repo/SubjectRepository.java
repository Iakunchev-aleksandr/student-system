package com.school.repo;

import com.school.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByOrderByName();
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
