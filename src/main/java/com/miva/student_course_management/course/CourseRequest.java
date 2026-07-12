package com.miva.student_course_management.course;

import jakarta.validation.constraints.*;

public record CourseRequest(
    @NotBlank
    @Size(max = 20)
    String code,

    @NotBlank
    @Size(max = 150)
    String name,

    @Size(max = 500)
    String description,

    @NotNull
    @Min(1)
    @Max(10)
    Integer creditUnits
) {
}