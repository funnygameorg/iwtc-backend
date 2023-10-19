package com.example.demo.common.web.auth;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ExpiredAuthenticationException extends BaseException {
    public ExpiredAuthenticationException() {
        super(
                "인증기간 만료",
                CustomErrorCode.EXPIRED_REMEMBER_ME,
                HttpStatus.UNAUTHORIZED
        );
    }
}
