package com.miva.student_course_management.course;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseFileService {

    private static final Path COURSE_FILE =
        Paths.get("data", "courses.csv");

    public void saveCoursesToFile(List<Course> courses) throws IOException {
        Files.createDirectories(COURSE_FILE.getParent());

        List<String> lines = new ArrayList<>();
        lines.add("code,name,description,creditUnits");

        for (Course course : courses) {
            lines.add(String.join(",",
                escape(course.getCode()),
                escape(course.getName()),
                escape(course.getDescription()),
                String.valueOf(course.getCreditUnits())
            ));
        }

        Files.write(
            COURSE_FILE,
            lines,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value.replace(",", " ");
    }
}