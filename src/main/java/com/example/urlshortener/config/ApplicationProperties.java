package com.example.urlshortener.config;

import com.example.urlshortener.entity.ShortUrl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final ShortUrl shortUrl = new ShortUrl();


@Getter
@Setter
    public static class ShortUrl{

        private int codeLength;

        private int defaultExpiryDays;

        private int maxGenerationAttempts;

        private String baseUrl;
    }

}
