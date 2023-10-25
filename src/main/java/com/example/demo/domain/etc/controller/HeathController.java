package com.example.demo.domain.etc.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathController {

    @GetMapping("/health")
    public String getHealth() {
        return "OK";
    }
}