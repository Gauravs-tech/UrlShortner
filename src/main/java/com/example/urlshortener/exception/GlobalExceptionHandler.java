package com.example.urlshortener.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ){
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }



    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUrlException(
            InvalidUrlException exception,
            HttpServletRequest request
    ){
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request
        );

        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ErrorResponse>handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Validation failed");

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception exception,
            HttpServletRequest request
    ){
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error Occured",
                request
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }


    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ){
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }
}
