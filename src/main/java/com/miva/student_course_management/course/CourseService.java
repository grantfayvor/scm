package com.miva.student_course_management.course;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll(
            Sort.by(Course::getCode).ascending()
        );
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException(id));
    }

    @Transactional
    public Course create(CourseRequest request) {
        String code = normalizeCode(request.code());

        if (courseRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException(
                "Course code already exists: " + code
            );
        }

        Course course = new Course(
            code,
            request.name().trim(),
            trimToNull(request.description()),
            request.creditUnits()
        );

        return courseRepository.save(course);
    }

    @Transactional
    public Course update(Long id, CourseRequest request) {
        Course course = findById(id);
        String code = normalizeCode(request.code());

        courseRepository.findByCodeIgnoreCase(code)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException(
                    "Course code already exists: " + code
                );
            });

        course.update(
            code,
            request.name().trim(),
            trimToNull(request.description()),
            request.creditUnits()
        );

        return course;
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.delete(findById(id));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank()
            ? null
            : value.trim();
    }
}
