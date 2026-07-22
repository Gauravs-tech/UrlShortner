package com.example.urlshortener.exception;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {


}
