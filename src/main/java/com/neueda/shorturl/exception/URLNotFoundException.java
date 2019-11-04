package com.neueda.shorturl.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@NoArgsConstructor
public class URLNotFoundException extends Exception {
    private String errorCode = "ST-002";
    private String userMessage = "URL does not exist, please make sure that it is the correct one.";
    private Integer httpStatus = NOT_FOUND.value();
    private String message = "URL at key %s does not exist.";
    
    public URLNotFoundException(Long urlId) {
        this.message = String.format(this.message, urlId);
    }
}
