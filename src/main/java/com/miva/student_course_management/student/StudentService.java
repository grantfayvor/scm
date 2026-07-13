package com.miva.student_course_management.student;

import com.miva.student_course_management.course.Course;
import com.miva.student_course_management.course.CourseRepository;
import com.miva.student_course_management.registration.*;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository registrationRepository;

    public StudentService(
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        CourseRegistrationRepository registrationRepository
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public Student create(StudentRequest request) {

        String matricNumber = request.matricNumber().trim().toUpperCase();

        if (studentRepository.existsByMatricNumberIgnoreCase(matricNumber)) {
            throw new IllegalArgumentException(
                "Student with matric number " + matricNumber + " already exists."
            );
        }

        Student student = new Student(
            matricNumber,
            request.name().trim()
        );

        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll(
            Sort.by(Student::getMatricNumber).ascending()
        );
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student findByMatricNumber(String matricNumber) {
        return studentRepository.findByMatricNumberIgnoreCase(matricNumber)
            .orElseThrow(() -> new StudentNotFoundException(matricNumber));
    }

    @Transactional
    public RegistrationResponse registerCourses(
        Long studentId,
        RegistrationRequest request
    ) {
        Student student = findStudent(studentId);

        Set<Long> courseIds = request.courseIds();
        List<Course> courses = courseRepository.findAllById(courseIds);

        if (courses.size() != courseIds.size()) {
            throw new IllegalArgumentException(
                "One or more selected courses do not exist"
            );
        }

        for (Course course : courses) {
            boolean alreadyRegistered =
                registrationRepository.existsByStudentIdAndCourseId(
                    studentId,
                    course.getId()
                );

            if (!alreadyRegistered) {
                registrationRepository.save(
                    new CourseRegistration(student, course)
                );
            }
        }

        return getRegisteredCourses(studentId);
    }

    public RegistrationResponse getRegisteredCourses(Long studentId) {
        findStudent(studentId);

        List<Course> courses = registrationRepository
            .findAllByStudentId(studentId)
            .stream()
            .map(CourseRegistration::getCourse)
            .toList();

        int totalCreditUnits = calculateTotalCreditUnitsRecursive(courses, 0);

        List<RegistrationResponse.RegisteredCourse> registeredCourses =
            courses.stream()
                .map(course ->
                    new RegistrationResponse.RegisteredCourse(
                        course.getId(),
                        course.getCode(),
                        course.getName(),
                        course.getCreditUnits()
                    )
                )
                .toList();

        return new RegistrationResponse(
            studentId,
            registeredCourses,
            totalCreditUnits
        );
    }

    private int calculateTotalCreditUnitsRecursive(List<Course> courses, int index) {
        if (index >= courses.size()) {
            return 0;
        }
        return courses.get(index).getCreditUnits()
            + calculateTotalCreditUnitsRecursive(courses, index + 1);
    }

    @Transactional
    public void unregisterCourse(Long studentId, Long courseId) {
        findStudent(studentId);

        if (!registrationRepository.existsByStudentIdAndCourseId(
            studentId,
            courseId
        )) {
            throw new IllegalArgumentException(
                "Student is not registered for this course"
            );
        }

        registrationRepository.deleteByStudentIdAndCourseId(
            studentId,
            courseId
        );
    }

    private Student findStudent(Long studentId) {
        return studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
}