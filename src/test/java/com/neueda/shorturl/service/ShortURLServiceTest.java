package com.neueda.shorturl.service;

import com.neueda.shorturl.exception.InvalidUrlException;
import com.neueda.shorturl.repository.ShortURLRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

public class ShortURLServiceTest {
    
    ShortURLRepository shortUrlRepository;
    private ShortURLService service;
    
    @Before
    public void init() {
        shortUrlRepository = Mockito.mock(ShortURLRepository.class);
        this.service = new ShortURLService(shortUrlRepository);
    }
    
    @Test(expected = InvalidUrlException.class)
    public void shortenURL_badFormat_throwException() throws Exception {
        String longUrl = RandomStringUtils.randomAlphanumeric(10);
        String localUrl = RandomStringUtils.randomAlphanumeric(10);
        
        service.shortenURL(localUrl, longUrl);
    }
    
    @Test
    public void shortenURL_return_shortenUrl() throws Exception {
        String longUrl = "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/";
        String localUrl = "http://localhost:8080/shorten";
        
        given(shortUrlRepository.incrementID()).willReturn(Long.valueOf(RandomStringUtils.randomNumeric(3)));
        doNothing().when(shortUrlRepository).saveUrl(anyString(), anyString());
        
        String actual = service.shortenURL(localUrl, longUrl).join();
        
        assertThat(actual).isNotNull();
        then(shortUrlRepository).should().incrementID();
        then(shortUrlRepository).should().saveUrl(anyString(), anyString());
    }
    
    @Test
    public void getLongURLFromID() throws Exception {
        Long uniqueId = 3L;
        String uniqueIdInput = "3L";
        String longUrl = "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/";
        given(shortUrlRepository.getUrl(uniqueId)).willReturn(longUrl);
        
        service.getLongURLFromID(uniqueIdInput).join();
        
        then(shortUrlRepository).should().getUrl(anyLong());
    }
}