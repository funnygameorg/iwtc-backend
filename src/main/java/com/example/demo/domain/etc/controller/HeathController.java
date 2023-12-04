package com.example.demo.domain.etc.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class HeathController {

    @ResponseStatus(OK)
    @GetMapping("/api/health")
    public String getHealth() {
        return "OK";
    }

}
