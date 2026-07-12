package com.miva.student_course_management.registration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RegistrationRequest(
    @NotEmpty
    Set<@NotNull Long> courseIds
) {
}