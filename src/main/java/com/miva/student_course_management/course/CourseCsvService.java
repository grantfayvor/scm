package com.miva.student_course_management.course;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CourseCsvService {

    private static final String[] HEADERS = {
        "code",
        "name",
        "description",
        "creditUnits"
    };

    private final CourseRepository courseRepository;
    private final CourseFileService courseFileService;

    public CourseCsvService(
        CourseRepository courseRepository,
        CourseFileService courseFileService
    ) {
        this.courseRepository = courseRepository;
        this.courseFileService = courseFileService;
    }

    public byte[] exportCourses() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (
            OutputStreamWriter writer = new OutputStreamWriter(
                output,
                StandardCharsets.UTF_8
            );
            CSVPrinter printer = new CSVPrinter(
                writer,
                CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .get()
            )
        ) {
            List<Course> courses = courseRepository.findAll();
            for (Course course : courses) {
                printer.printRecord(
                    course.getCode(),
                    course.getName(),
                    course.getDescription(),
                    course.getCreditUnits()
                );
            }
            courseFileService.saveCoursesToFile(courses);
        }

        return output.toByteArray();
    }

    @Transactional
    public int importCourses(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Select a CSV file.");
        }

        int importedCount = 0;

        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true)
            .get();

        try (
            Reader reader = new InputStreamReader(
                file.getInputStream(),
                StandardCharsets.UTF_8
            )
        ) {
            List<CSVRecord> records = format.parse(reader).getRecords();

            for (CSVRecord record : records) {
                String code = record.get("code").trim().toUpperCase();

                if (code.isBlank()
                    || courseRepository.existsByCodeIgnoreCase(code)) {
                    continue;
                }

                String name = record.get("name").trim();
                String description = record.get("description").trim();
                int creditUnits = Integer.parseInt(
                    record.get("creditUnits").trim()
                );

                Course course = new Course(
                    name,
                    description.isBlank() ? null : description,
                    code,
                    creditUnits
                );

                courseRepository.save(course);
                importedCount++;
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                "The CSV contains an invalid creditUnits value."
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                "Invalid CSV format. Required columns: "
                    + String.join(", ", HEADERS)
            );
        }

        return importedCount;
    }
}