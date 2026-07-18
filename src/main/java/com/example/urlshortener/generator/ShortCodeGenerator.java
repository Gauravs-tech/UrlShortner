package com.example.urlshortener.generator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {
    private static final String BASE62 =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final SecureRandom secureRandom = new SecureRandom();

    private final int codeLength;

    public ShortCodeGenerator(
            @Value("${application.short-url.code-length}") int codeLength
    ){
        this.codeLength = codeLength;
    }

    public String generate(){
        StringBuilder shortCode = new StringBuilder(codeLength);

        for (int i = 0; i< codeLength; i++){
            int index = secureRandom.nextInt(BASE62.length());

            shortCode.append(BASE62.charAt(index));
        }
        return shortCode.toString();
    }

}
