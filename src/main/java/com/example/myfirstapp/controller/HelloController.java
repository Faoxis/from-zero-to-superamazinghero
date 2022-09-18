package com.example.myfirstapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/where/{number}")
    public String sayHello(@PathVariable(name = "number") int number) {

        return String.valueOf(number * number);
    }

}
