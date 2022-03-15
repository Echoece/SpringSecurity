package com.echo.springsecurity.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "karim"),
            new Student(2, "Rafiq"),
            new Student(3, "Rahim")
    );

    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        return STUDENTS.stream()
                .filter(element -> studentId.equals(element.getStudentId()))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("Student " + studentId + " does not exist"));
    }
}
