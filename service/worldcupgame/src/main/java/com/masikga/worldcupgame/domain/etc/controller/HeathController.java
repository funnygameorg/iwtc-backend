package com.masikga.worldcupgame.domain.etc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class HeathController {

	@ResponseStatus(OK)
	@GetMapping
	public String getHealth() {
		return "OK!";
	}

}
