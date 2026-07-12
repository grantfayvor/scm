package com.miva.student_course_management.registration;

import com.miva.student_course_management.course.Course;
import com.miva.student_course_management.student.Student;
import jakarta.persistence.*;

@Entity
@Table(
    name = "course_registrations",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_student_course",
        columnNames = {"student_id", "course_id"}
    )
)
public class CourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    protected CourseRegistration() {
    }

    public CourseRegistration(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }
}