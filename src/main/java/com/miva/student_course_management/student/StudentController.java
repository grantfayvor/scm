package com.miva.student_course_management.student;

import com.miva.student_course_management.registration.*;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(
        @Valid @RequestBody StudentRequest request
    ) {
        return studentService.create(request);
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(
        @PathVariable Long id
    ) {
        return studentService.findById(id);
    }

    @PostMapping("/{studentId}/courses")
    public RegistrationResponse registerCourses(
        @PathVariable Long studentId,
        @Valid @RequestBody RegistrationRequest request
    ) {
        return studentService.registerCourses(studentId, request);
    }

    @GetMapping("/{studentId}/courses")
    public RegistrationResponse getRegisteredCourses(
        @PathVariable Long studentId
    ) {
        return studentService.getRegisteredCourses(studentId);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> unregisterCourse(
        @PathVariable Long studentId,
        @PathVariable Long courseId
    ) {
        studentService.unregisterCourse(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({
        StudentNotFoundException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<String> handleException(
        RuntimeException exception
    ) {
        return ResponseEntity
            .badRequest()
            .body(exception.getMessage());
    }
}