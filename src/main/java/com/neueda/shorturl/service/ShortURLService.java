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
public class ShortURLService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortURLService.class);
    public static final String URL_KEY = "url:";
    private final ShortURLRepository shortUrlRepository;
    
    @Autowired
    public ShortURLService(ShortURLRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }
    
    /**
     *
     * @param localURL is the url associated with the local server.
     * @param longUrl the original url
     * @return the shorten url version
     * @throws Exception if the url format is incorrect.
     */
    public CompletableFuture<String> shortenURL(String localURL, String longUrl) throws Exception {
        if (!URLValidator.INSTANCE.validateURL(longUrl)) {
            throw new InvalidUrlException();
        }
        LOGGER.info("Shortening {}", longUrl);
        Long id = getShortenId();
        shortUrlRepository.saveUrl(URL_KEY + id, longUrl);
        String baseString = formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + URLIDUtils.INSTANCE.createUniqueID(id);;
        return CompletableFuture.completedFuture(shortenedURL);
    }
    
    /**
     *
     * @param uniqueID the unique identifier per each url.
     * @return the original url.
     * @throws Exception if the url is not found.
     */
    public CompletableFuture<String> getLongURLFromID(String uniqueID) throws Exception {
        Long dictionaryKey = URLIDUtils.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = shortUrlRepository.getUrl(dictionaryKey);
        LOGGER.info("Converting shortened URL back to {}", longUrl);
        return CompletableFuture.completedFuture(longUrl);
    }
    
    /**
     * Generates a new unique id for each new url.
     * @return the unique id.
     */
    private Long getShortenId() {
        return shortUrlRepository.incrementID();
    }
    
    /**
     * Format the local url to adding the shorten url instead of the endpoint.
     * @param localURL
     * @return the localUrl which will be append to the shorten one.
     */
    private String formatLocalURLFromShortener(String localURL) {
        String[] addressComponents = localURL.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addressComponents.length - 1; ++i) {
            sb.append(addressComponents[i]);
        }
        sb.append('/');
        return sb.toString();
    }
}
