package com.demo1.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    String sayHello()
    {
        return "Hello World!";
    }
    @GetMapping("/subika")
    String myname()
    {
        return "Subika is a Legend!";
    }

}

