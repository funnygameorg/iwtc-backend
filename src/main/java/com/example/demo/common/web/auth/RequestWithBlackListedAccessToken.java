package com.example.demo.common.web.auth;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RequestWithBlackListedAccessToken extends BaseException {
    public RequestWithBlackListedAccessToken(String accessToken) {
        super(
                new StringBuilder().append("[사용할수 없는 토큰]").append(accessToken).toString(),
                REQUEST_WITH_BLACK_LIST_TOKEN,
                UNAUTHORIZED
        );

    }
}
