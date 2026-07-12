package com.miva.student_course_management.course;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> findAll() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public Course findById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Course> create(
        @Valid @RequestBody CourseRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.create(request));
    }

    @PutMapping("/{id}")
    public Course update(
        @PathVariable Long id,
        @Valid @RequestBody CourseRequest request
    ) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleNotFound(
        CourseNotFoundException exception
    ) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(
        IllegalArgumentException exception
    ) {
        return ResponseEntity
            .badRequest()
            .body(exception.getMessage());
    }
}
