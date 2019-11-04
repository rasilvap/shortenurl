package com.neueda.shorturl.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortenUrlDto {
    private String url;
    
    @JsonCreator
    public ShortenUrlDto() { }
    
    @JsonCreator
    public ShortenUrlDto(@JsonProperty("url") String url) {
        this.url = url;
    }
}
