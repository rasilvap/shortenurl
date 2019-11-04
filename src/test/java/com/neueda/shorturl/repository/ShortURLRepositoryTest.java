package com.neueda.shorturl.repository;

import ai.grakn.redismock.RedisServer;
import com.neueda.shorturl.exception.URLNotFoundException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class ShortURLRepositoryTest {
    
    public static final String ID_KEY = "id";
    public static final String URL_KEY = "url:";
    private RedisServer server;
    private Jedis mockJedis;
    
    @Before
    public void setupServer() throws IOException {
        mockJedis = Mockito.mock(Jedis.class);
        server = RedisServer.newRedisServer(6789);
        server.start();
    }
    
    @After
    public void shutdownServer() throws IOException {
        server.stop();
    }
    
    @Test
    public void test_incrementID_StartsAt0AndIncrementsByOne() {
        ShortURLRepository urlRepository = new ShortURLRepository(new Jedis(server.getHost(), server.getBindPort())
                , ID_KEY, URL_KEY);
        for (long expectedId = 0L; expectedId < 50L; ++expectedId) {
            long actualId = urlRepository.incrementID();
            assertEquals(expectedId, actualId);
        }
    }
    
    @Test
    public void getUrl_returnUrlFromStore() throws Exception {
        String expectedUrl = RandomStringUtils.randomAlphabetic(10);
        mockJedis = Mockito.mock(Jedis.class);
        given(mockJedis.hget(anyString(), anyString())).willReturn(expectedUrl);
        ShortURLRepository urlRepository = new ShortURLRepository(mockJedis, ID_KEY, URL_KEY);
        String actuaUrl = urlRepository.getUrl(Long.valueOf(RandomStringUtils.randomNumeric(3)));
        assertEquals(expectedUrl, actuaUrl);
        then(mockJedis).should().hget(anyString(), anyString());
    }
    
    @Test(expected = URLNotFoundException.class)
    public void getUrl_doesNotExist_throwException() throws Exception {
        mockJedis = Mockito.mock(Jedis.class);
        given(mockJedis.hget(anyString(), anyString())).willReturn(null);
        ShortURLRepository urlRepository = new ShortURLRepository(mockJedis, ID_KEY, URL_KEY);
        urlRepository.getUrl(Long.valueOf(RandomStringUtils.randomNumeric(3)));
        then(mockJedis).should().hget(anyString(), anyString());
    }
}