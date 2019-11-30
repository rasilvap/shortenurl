package com.neueda.shorturl.service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortenUrlDto {
    private String url;
    
    public ShortenUrlDto(String url) {
        this.url = url;
    }
}
