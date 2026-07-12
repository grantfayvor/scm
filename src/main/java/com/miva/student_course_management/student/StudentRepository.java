package com.miva.student_course_management.student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByMatricNumberIgnoreCase(String matricNumber);
}