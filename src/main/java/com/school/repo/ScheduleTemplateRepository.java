package com.school.repo;

import com.school.model.AppUser;
import com.school.model.ScheduleTemplate;
import com.school.model.SchoolClass;
import com.school.model.Subject;
import com.school.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, Long> {
    List<ScheduleTemplate> findAllByOrderByDayOfWeekAscStartTimeAsc();
    List<ScheduleTemplate> findByTeacher(AppUser teacher);
    List<ScheduleTemplate> findBySchoolClass(SchoolClass schoolClass);

    boolean existsByTeacher(AppUser teacher);
    boolean existsBySchoolClass(SchoolClass schoolClass);
    boolean existsBySubject(Subject subject);
    boolean existsByTerm(Term term);
}
