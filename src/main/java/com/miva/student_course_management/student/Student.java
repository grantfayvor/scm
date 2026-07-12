package com.miva.student_course_management.student;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricNumber;

    @Column(nullable = false)
    private String name;

    protected Student() {
    }

    public Student(String matricNumber, String name) {
        this.matricNumber = matricNumber;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getName() {
        return name;
    }
}