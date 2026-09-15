package com.school.repo;

import com.school.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByOrderByStartDateAsc();
}
