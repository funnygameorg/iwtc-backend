package com.example.demo.common.jwt;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationTokenException extends BaseException {
    public AuthenticationTokenException(String message)
    {
        super(
                INVALID_TOKEN_EXCEPTION.getMessage() + " " + message,
                INVALID_TOKEN_EXCEPTION,
                INVALID_TOKEN_EXCEPTION.getHttpStatus()
        );
    }
}
