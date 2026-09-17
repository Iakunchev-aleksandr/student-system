package com.school.repo;

import com.school.model.AppUser;
import com.school.model.Role;
import com.school.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmailIgnoreCase(String email);
    List<AppUser> findByRole(Role role);
    List<AppUser> findBySchoolClassOrderByLastNameAscFirstNameAsc(SchoolClass schoolClass);
    List<AppUser> findByRoleOrderByLastNameAscFirstNameAsc(Role role);

    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByStudentNumber(String studentNumber);
    boolean existsByStudentNumberAndIdNot(String studentNumber, Long id);
    boolean existsBySchoolClass(SchoolClass schoolClass);
}
