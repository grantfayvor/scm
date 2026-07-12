package com.miva.student_course_management.course;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long id) {
        super("Course not found with ID: " + id);
    }
}
