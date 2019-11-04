package com.neueda.shorturl.service;

import com.neueda.shorturl.exception.InvalidUrlException;
import com.neueda.shorturl.repository.ShortURLRepository;
import com.neueda.shorturl.util.URLIDUtils;
import com.neueda.shorturl.util.URLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class URLConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLConverterService.class);
    private final ShortURLRepository shortUrlRepository;
    
    @Autowired
    public URLConverterService(ShortURLRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }
    
    public CompletableFuture<String> shortenURL(String localURL, String longUrl) throws Exception {
        if (!URLValidator.INSTANCE.validateURL(longUrl)) {
            throw new InvalidUrlException();
        }
        LOGGER.info("Shortening {}", longUrl);
        Long id = shortUrlRepository.incrementID();
        String uniqueID = URLIDUtils.INSTANCE.createUniqueID(id);
        shortUrlRepository.saveUrl("url:"+id, longUrl);
        String baseString = formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + uniqueID;
        return CompletableFuture.completedFuture(shortenedURL);
    }
    
    public CompletableFuture<String> getLongURLFromID(String uniqueID) throws Exception {
        Long dictionaryKey = URLIDUtils.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = shortUrlRepository.getUrl(dictionaryKey);
        LOGGER.info("Converting shortened URL back to {}", longUrl);
        return CompletableFuture.completedFuture(longUrl);
    }
    
    private String formatLocalURLFromShortener(String localURL) {
        String[] addressComponents = localURL.split("/");
        // remove the endpoint (last index)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addressComponents.length - 1; ++i) {
            sb.append(addressComponents[i]);
        }
        sb.append('/');
        return sb.toString();
    }
}
