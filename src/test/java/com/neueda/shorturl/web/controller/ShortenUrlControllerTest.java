package com.neueda.shorturl.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neueda.shorturl.exception.URLNotFoundException;
import com.neueda.shorturl.service.ShortURLService;
import com.neueda.shorturl.service.dto.ShortenUrlDto;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortenUrlController.class)
public class ShortenUrlControllerTest {
    
    
    public static final String SHORTEN_URL_PATH = "/shorten";
    public static final String URL_FIND_PATH = "/{id}";
    @Autowired
    protected MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ShortURLService shortURLService;
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void short_url_BadRequest() throws Exception {
        executeShortenRequest(null, status().isBadRequest());
    }
    
    @Test
    public void short_url_shortenUrlIsCreated() throws Exception {
        ShortenUrlDto shortenUrlDto = buildShortenUrlDto();
        String shortenUrl = "http://localhost:8080/b2";
        
        given(shortURLService.shortenURL(anyString(), anyString())).willReturn(CompletableFuture.completedFuture(shortenUrl));
        
        executeShortenRequest(shortenUrlDto, status().isCreated());
        then(shortURLService).should().shortenURL(anyString(), anyString());
    }
    
    @Test
    public void findOriginalUrlById_urlIsRetorned() throws Exception {
        String longUrl = "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/";
        
        given(shortURLService.getLongURLFromID(anyString())).willReturn(CompletableFuture.completedFuture(longUrl));
        
        MvcResult result = executeFindRequest(RandomStringUtils.randomAlphabetic(10));
        mvc.perform(MockMvcRequestBuilders.asyncDispatch(result))
                .andExpect(status().isOk());
    }
    
    @Test
    public void findOriginalUrlById_urlIsNotFound() throws Exception {
        CompletableFuture<String> url = new CompletableFuture<>();
        url.completeExceptionally(new URLNotFoundException());
        
        given(shortURLService.getLongURLFromID(anyString())).willReturn(url);
        
        MvcResult result = executeFindRequest(RandomStringUtils.randomAlphabetic(10));
    }
    
    private void executeShortenRequest(ShortenUrlDto shortenUrlDto, ResultMatcher expectedStatus) throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(SHORTEN_URL_PATH)
                .content(mapper.writeValueAsString(shortenUrlDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(expectedStatus);
    }
    
    private MvcResult executeFindRequest(String urlId) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_PATH, urlId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(request().asyncStarted())
                .andReturn();
    }
    
    private ShortenUrlDto buildShortenUrlDto() {
        return ShortenUrlDto.builder()
                .url("https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/")
                .build();
    }
}