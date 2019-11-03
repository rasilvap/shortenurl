package com.sovanm.docker.shortenurl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenUrlController {

	@GetMapping("/name")
	public String getMyName() {
		
		return "Simple Spring Boot Application";
	}
}
