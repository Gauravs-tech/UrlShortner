package com.example.urlshortener.service;

import com.example.urlshortener.config.ApplicationProperties;
import com.example.urlshortener.dto.request.CreateShortUrlRequest;
import com.example.urlshortener.dto.response.CreateShortUrlResponse;
import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.exception.InvalidUrlException;
import com.example.urlshortener.exception.ResourceNotFoundException;
import com.example.urlshortener.exception.ShortCodeGenerationException;
import com.example.urlshortener.generator.ShortCodeGenerator;
import com.example.urlshortener.mapper.ShortUrlMapper;
import com.example.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortUrlService {

    private final ShortUrlRepository repository;
    private final ShortUrlMapper mapper;
    private final ShortCodeGenerator shortCodeGenerator;
    private final ApplicationProperties applicationProperties;

    private void validateUrl(String originalUrl){
        try{
            URI uri = URI.create(originalUrl);

            String scheme = uri.getScheme();

            if (!"http".equalsIgnoreCase(scheme) &&
                !"https".equalsIgnoreCase(scheme)){

                throw new InvalidUrlException("Only HTTP and HTTPS URLs are supported.");
            }
        }
        catch (IllegalArgumentException ex){
            throw new InvalidUrlException("Invalid URL format");
        }
    }

    private String generateUniqueShortCode(){
        int maxAttempts = applicationProperties
                .getShortUrl()
                .getMaxGenerationAttempts();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            String shortCode = shortCodeGenerator.generate();

            if (!repository.existsByShortCode(shortCode)) {
                return shortCode;
            }
        }

        throw new ShortCodeGenerationException(
                "Unable to generate a unique short code."
        );
    }

    private ShortUrl buildEntity(
            CreateShortUrlRequest request,
            String shortCode
    ){
        int expiryDays = applicationProperties
                .getShortUrl()
                .getDefaultExpiryDays();

        return ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .clickCount(0L)
                .active(true)
                .expiresAt(OffsetDateTime.now().plusDays(expiryDays))
                .build();
    }

    private String buildShortUrl(String shortCode){
        return  applicationProperties.getShortUrl().getBaseUrl()+"/"+shortCode;
    }

    @Transactional
    public String getOriginalUrl(String shortCode) {

        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Short URL not found."
                ));

        if (!shortUrl.isActive()) {
            throw new ResourceNotFoundException("Short URL is inactive.");
        }

        if (shortUrl.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new ResourceNotFoundException("Short URL has expired.");
        }

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);

        repository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }

    public CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request) {

        validateUrl((request.getOriginalUrl()));

        String shortCode = generateUniqueShortCode();

        ShortUrl shortUrl = buildEntity(request,shortCode);

        ShortUrl savedShortUrl = repository.save(shortUrl);

        String shortUrlValue = buildShortUrl(savedShortUrl.getShortCode());

        return mapper.toResponse(savedShortUrl,shortUrlValue);

    }
}
