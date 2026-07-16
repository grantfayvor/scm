package com.miva.student_course_management.course;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CoursePageController {

    private final CourseService courseService;
    private final CourseCsvService courseCsvService;

    public CoursePageController(
        CourseService courseService,
        CourseCsvService courseCsvService
    ) {
        this.courseService = courseService;
        this.courseCsvService = courseCsvService;
    }

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);

        int totalCreditUnits = courses.stream()
            .mapToInt(Course::getCreditUnits)
            .sum();
        model.addAttribute("totalCreditUnits", totalCreditUnits);
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

    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportCourses() throws IOException {
        byte[] csv = courseCsvService.exportCourses();
        ByteArrayResource resource = new ByteArrayResource(csv);

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"courses.csv\""
            )
            .contentType(MediaType.parseMediaType("text/csv"))
            .contentLength(csv.length)
            .body(resource);
    }

    @PostMapping("/import")
    public String importCourses(
        @RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes
    ) {
        try {
            int importedCount = courseCsvService.importCourses(file);

            redirectAttributes.addFlashAttribute(
                "success",
                importedCount + " courses imported successfully."
            );
        } catch (IOException | IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                "error",
                exception.getMessage()
            );
        }

        return "redirect:/courses";
    }
}