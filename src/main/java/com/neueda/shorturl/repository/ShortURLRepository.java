package com.neueda.shorturl.repository;

import com.neueda.shorturl.exception.URLNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class ShortURLRepository {
    public static final String REDIS_HOST = "redis";
    public static final int REDIS_PORT = 6379;
    public static final String ID_KEY = "id";
    public static final String URL_KEY = "url:";
    private final Jedis jedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortURLRepository.class);
    
    public ShortURLRepository() {
        this.jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        this.idKey = ID_KEY;
        this.urlKey = URL_KEY;
    }
    
    public ShortURLRepository(Jedis jedis, String idKey, String urlKey) {
        this.jedis = jedis;
        this.idKey = idKey;
        this.urlKey = urlKey;
    }
    
    /**
     * This method find in the redis storage and generates a new unique ID for each new url.
     * @return next uniqueID per each url
     */
    public Long incrementID() {
        Long id = jedis.incr(idKey);
        LOGGER.info("Incrementing ID: {}", id - 1);
        return id - 1;
    }
    
    /**
     *
     * @param key the unique identifier of the shorten url.
     * @param longUrl the original url
     */
    public void saveUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", longUrl, key);
        jedis.hset(urlKey, key, longUrl);
    }
    
    /**
     *
     * @param id the id associated with the original url and the shorten version.
     * @return the original url.
     * @throws Exception if the url is not found.
     */
    public String getUrl(Long id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, "url:" + id);
        LOGGER.info("Retrieved {} at {}", url, id);
        if (url == null) {
            throw new URLNotFoundException(id);
        }
        return url;
    }
}
