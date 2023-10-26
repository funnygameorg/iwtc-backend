package com.example.demo.common.jwt;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationTokenException extends BaseException {
    public AuthenticationTokenException(String message)
    {
        super(
                message,
                CustomErrorCode.INVALID_TOKEN_EXCEPTION,
                UNAUTHORIZED
        );
    }
}
