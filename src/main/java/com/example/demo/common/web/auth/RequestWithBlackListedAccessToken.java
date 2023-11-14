package com.example.demo.common.web.auth;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RequestWithBlackListedAccessToken extends BaseException {
    public RequestWithBlackListedAccessToken(String accessToken) {
        super(
                REQUEST_WITH_BLACK_LIST_TOKEN.getMessage() +" "+ accessToken,
                REQUEST_WITH_BLACK_LIST_TOKEN,
                REQUEST_WITH_BLACK_LIST_TOKEN.getHttpStatus()
        );

    }
}
