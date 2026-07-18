package com.example.urlshortener.controller;

import com.example.urlshortener.dto.request.CreateShortUrlRequest;
import com.example.urlshortener.dto.response.CreateShortUrlResponse;
import com.example.urlshortener.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateShortUrlResponse createShortUrl(@Valid
                                                 @RequestBody
                                                 CreateShortUrlRequest request){
        return shortUrlService.createShortUrl(request);
    }

}
