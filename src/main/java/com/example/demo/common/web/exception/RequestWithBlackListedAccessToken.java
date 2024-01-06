package com.example.demo.common.web.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class RequestWithBlackListedAccessToken extends BaseException {
	public RequestWithBlackListedAccessToken(String accessToken) {
		super(
			REQUEST_WITH_BLACK_LIST_TOKEN.getMessage() + " " + accessToken,
			REQUEST_WITH_BLACK_LIST_TOKEN,
			REQUEST_WITH_BLACK_LIST_TOKEN.getHttpStatus()
		);

	}
}
