package com.example.urlshortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateShortUrlRequest {

    @NotBlank(message = "Original url is required.")
    @Size(max = 2048, message = "URL must not exceed 2048 character.")
    private String originalUrl;
}
