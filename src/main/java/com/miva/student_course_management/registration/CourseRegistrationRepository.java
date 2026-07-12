package com.miva.student_course_management.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRegistrationRepository
    extends JpaRepository<CourseRegistration, Long> {

    List<CourseRegistration> findAllByStudentId(Long studentId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}