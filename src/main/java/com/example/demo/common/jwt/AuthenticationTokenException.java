package com.example.demo.common.jwt;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.INVALID_TOKEN_EXCEPTION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationTokenException extends BaseException {
    public AuthenticationTokenException(String message)
    {
        super(
                message,
                INVALID_TOKEN_EXCEPTION,
                UNAUTHORIZED
        );
    }
}
