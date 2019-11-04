package com.neueda.shorturl.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortenUrlDto {
    private String url;
    
    @JsonCreator
    public ShortenUrlDto() {
    
    }
    
    @JsonCreator
    public ShortenUrlDto(@JsonProperty("url") String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
