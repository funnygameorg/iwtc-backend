package com.example.demo.global.exception;

import com.example.demo.global.web.RestApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity webRequestValidException(
            MethodArgumentNotValidException ex
    ) {
        String invalidRequestMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList()
                .toString();

        return ResponseEntity
                .badRequest()
                .body(
                        new RestApiResponse(
                                0, invalidRequestMessage, null
                        )
                );
    }
}
