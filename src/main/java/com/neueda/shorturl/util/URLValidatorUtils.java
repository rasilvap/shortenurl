package com.neueda.shorturl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLValidatorUtils {
    public static final URLValidatorUtils INSTANCE = new URLValidatorUtils();
    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
    
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
    
    private URLValidatorUtils() {
    }
    
    /**
     * Validates if the url format is correct.
     * @param url the original url.
     * @return true if the url is correct, otherwise false.
     */
    public boolean validateURL(String url) {
        Matcher m = URL_PATTERN.matcher(url);
        return m.matches();
    }
}
