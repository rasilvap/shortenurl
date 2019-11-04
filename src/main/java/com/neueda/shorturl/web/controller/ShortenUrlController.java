package com.neueda.shorturl.web.controller;

import com.neueda.shorturl.service.ShortURLService;
import com.neueda.shorturl.service.dto.ShortenUrlDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
public class ShortenUrlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortenUrlController.class);
	public static final String SHORTEN_URL_PATH = "/shorten";
	public static final String URL_PATH = "/{id}";
	private final ShortURLService shortUrlService;
	
	public ShortenUrlController(ShortURLService shortUrlService) {
		this.shortUrlService = shortUrlService;
	}
	
	@PostMapping(SHORTEN_URL_PATH)
	@ResponseStatus(HttpStatus.CREATED)
	public CompletableFuture<String> shortenUrl(@RequestBody @Valid final ShortenUrlDto shortenUrlDto, HttpServletRequest request) throws Exception {
		LOGGER.info("Url to shorten: " + shortenUrlDto.getUrl());
		CompletableFuture<String> shortenedUrl = null;
		String localURL = request.getRequestURL().toString();
		shortenedUrl = shortUrlService.shortenURL(localURL, shortenUrlDto.getUrl());
		LOGGER.info("Shortened url to: " + shortenedUrl);
		return shortenedUrl;
	}
	
	@GetMapping(URL_PATH)
	public CompletableFuture<String> retrieveOriginalUrl(@PathVariable String id) throws Exception {
		LOGGER.info("short url to redirect: " + id);
		CompletableFuture<String> redirectUrlString = shortUrlService.getLongURLFromID(id);
		LOGGER.info("Original URL: " + redirectUrlString);
		return redirectUrlString;
	}
}

