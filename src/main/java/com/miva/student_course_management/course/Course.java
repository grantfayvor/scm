package com.miva.student_course_management.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "courses",
    uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer creditUnits;

    protected Course() {
    }

    public Course(String name, String description, String code, Integer creditUnits) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.creditUnits = creditUnits;
    }

    public void update(
        String code,
        String name,
        String description,
        Integer creditUnits
    ) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.creditUnits = creditUnits;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public Integer getCreditUnits() {
        return creditUnits;
    }
}
