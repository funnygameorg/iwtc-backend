package com.example.demo.domain.etc.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathController {

	@ResponseStatus(OK)
	@GetMapping
	public String getHealth() {
		return "OK!";
	}

}
