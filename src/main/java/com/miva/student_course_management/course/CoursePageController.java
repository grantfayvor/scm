package com.miva.student_course_management.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CoursePageController {

    private final CourseService courseService;

    public CoursePageController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "courses/list";
    }

    @GetMapping("/search")
    public String searchCourseByCode(
        @RequestParam(required = false) String code,
        Model model
    ) {
        if (code == null || code.trim().isEmpty()) {
            return listCourses(model);
        }

        model.addAttribute(
            "courses",
            courseService.searchByCode(code.trim())
        );
        return "courses/list";
    }

    @GetMapping("/new")
    public String newCourseForm() {
        return "courses/new";
    }

    @PostMapping
    public String createCourse(
        @RequestParam String code,
        @RequestParam String name,
        @RequestParam(required = false) String description,
        @RequestParam Integer creditUnits,
        RedirectAttributes redirectAttributes
    ) {
        try {
            courseService.create(
                new CourseRequest(
                    code,
                    name,
                    description,
                    creditUnits
                )
            );

            redirectAttributes.addFlashAttribute(
                "success",
                "Course created successfully."
            );

            return "redirect:/courses";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                "error",
                exception.getMessage()
            );

            return "redirect:/courses/new";
        }
    }
}