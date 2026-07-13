package com.miva.student_course_management.student;

import com.miva.student_course_management.course.CourseService;
import com.miva.student_course_management.registration.RegistrationRequest;
import com.miva.student_course_management.registration.RegistrationResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/students")
public class StudentPageController {

    private final StudentService studentService;
    private final CourseService courseService;

    public StudentPageController(
        StudentService studentService,
        CourseService courseService
    ) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    @GetMapping("/new")
    public String newStudentForm() {
        return "students/new";
    }

    @PostMapping
    public String createStudent(
        @RequestParam String matricNumber,
        @RequestParam String name,
        RedirectAttributes redirectAttributes
    ) {
        try {
            studentService.create(
                new StudentRequest(matricNumber, name)
            );

            redirectAttributes.addFlashAttribute(
                "success",
                "Student created successfully."
            );

            return "redirect:/students";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                "error",
                exception.getMessage()
            );

            return "redirect:/students/new";
        }
    }

    @GetMapping("/{studentId}/courses")
    public String registrationPage(
        @PathVariable Long studentId,
        Model model
    ) {
        model.addAttribute(
            "student",
            studentService.findById(studentId)
        );

        RegistrationResponse registeredCourses = studentService.getRegisteredCourses(studentId);
        List<Long> registeredCourseIds = registeredCourses.courses().stream()
            .map(RegistrationResponse.RegisteredCourse::id)
            .toList();

        model.addAttribute(
            "courses",
            courseService.findAllExcept(registeredCourseIds)
        );

        model.addAttribute(
            "registration",
            registeredCourses
        );

        return "students/register";
    }

    @PostMapping("/{studentId}/courses")
    public String registerCourses(
        @PathVariable Long studentId,
        @RequestParam(required = false) Set<Long> courseIds,
        RedirectAttributes redirectAttributes
    ) {
        if (courseIds == null || courseIds.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Select at least one course."
            );

            return "redirect:/students/" + studentId + "/courses";
        }

        try {
            studentService.registerCourses(
                studentId,
                new RegistrationRequest(courseIds)
            );

            redirectAttributes.addFlashAttribute(
                "success",
                "Courses registered successfully."
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                "error",
                exception.getMessage()
            );
        }

        return "redirect:/students/" + studentId + "/courses";
    }

    @PostMapping("/{studentId}/courses/{courseId}/delete")
    public String unregisterCourse(
        @PathVariable Long studentId,
        @PathVariable Long courseId,
        RedirectAttributes redirectAttributes
    ) {
        studentService.unregisterCourse(studentId, courseId);

        redirectAttributes.addFlashAttribute(
            "success",
            "Course removed successfully."
        );

        return "redirect:/students/" + studentId + "/courses";
    }
}