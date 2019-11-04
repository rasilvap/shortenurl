package com.neueda.shorturl.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@NoArgsConstructor
public class InvalidUrlException extends Exception {
    private String errorCode = "ST-001";
    private String userMessage = "Please enter a valid URL.";
    private Integer httpStatus = NOT_FOUND.value();
    private String message = "Url Format error.";
}
