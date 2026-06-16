package com.school.repo;

import com.school.model.AppUser;
import com.school.model.SchoolClass;
import com.school.model.SchoolEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SchoolEventRepository extends JpaRepository<SchoolEvent, Long> {
    List<SchoolEvent> findBySchoolClassAndDate(SchoolClass schoolClass, LocalDate date);
    List<SchoolEvent> findBySchoolClassAndDateBetweenOrderByDate(SchoolClass schoolClass, LocalDate from, LocalDate to);
    boolean existsBySchoolClass(SchoolClass schoolClass);
    boolean existsByCreatedBy(AppUser createdBy);
}
