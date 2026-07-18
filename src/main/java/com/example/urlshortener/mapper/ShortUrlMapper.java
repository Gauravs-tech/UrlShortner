package com.example.urlshortener.mapper;

import com.example.urlshortener.dto.request.CreateShortUrlRequest;
import com.example.urlshortener.dto.response.CreateShortUrlResponse;
import com.example.urlshortener.entity.ShortUrl;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlMapper {
    public ShortUrl toEntity(CreateShortUrlRequest request){
        return ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .build();
    }

    public CreateShortUrlResponse toResponse(
            ShortUrl shortUrl,
            String shortUrlValue
    ){
        return new CreateShortUrlResponse(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrlValue,
                shortUrl.getOriginalUrl(),
                shortUrl.getExpiresAt()
                );
    }
}
