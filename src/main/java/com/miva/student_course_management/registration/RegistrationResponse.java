package com.miva.student_course_management.registration;

import java.util.List;

public record RegistrationResponse(
    Long studentId,
    List<RegisteredCourse> courses,
    int totalCreditUnits
) {

    public record RegisteredCourse(
        Long id,
        String code,
        String name,
        int creditUnits
    ) {
    }
}