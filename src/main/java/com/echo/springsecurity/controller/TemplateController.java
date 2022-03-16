package com.echo.springsecurity.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView(){
        return "login";             // thymeleaf -> use same name as the html page in the templates folder.
    }

    @GetMapping("courses")
    public String getCourses(){
        return "courses";
    }
}
