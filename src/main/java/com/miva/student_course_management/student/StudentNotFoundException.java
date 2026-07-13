package com.miva.student_course_management.student;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Student not found with ID: " + id);
    }

    public StudentNotFoundException(String matricNumber) {
        super("Student not found with matric number: " + matricNumber);
    }
}