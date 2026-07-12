package com.miva.student_course_management.student;

import jakarta.validation.constraints.NotBlank;

public record StudentRequest(

    @NotBlank
    String matricNumber,

    @NotBlank
    String name

) {
}