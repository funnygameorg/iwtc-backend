package com.example.demo.common.web.auth;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.*;

public class ExpiredAuthenticationException extends BaseException {
    public ExpiredAuthenticationException() {
        super(
                EXPIRED_REMEMBER_ME.getMessage(),
                EXPIRED_REMEMBER_ME,
                EXPIRED_REMEMBER_ME.getHttpStatus()
        );
    }
}
