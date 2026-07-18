package com.example.urlshortener.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CreateShortUrlResponse {

    private Long id;

    private String shortCode;

    private String shortUrl;

    private String originalUrl;

    private OffsetDateTime expiresAt;

}
