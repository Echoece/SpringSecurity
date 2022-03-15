package com.echo.springsecurity.student;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Student {
    private final Integer studentId;
    private final String name;

    public Student(Integer studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }
}
