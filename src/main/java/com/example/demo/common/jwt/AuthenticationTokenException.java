package com.example.demo.common.jwt;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class AuthenticationTokenException extends BaseException {
	public AuthenticationTokenException(String message) {
		super(
			INVALID_TOKEN_EXCEPTION.getMessage() + " " + message,
			INVALID_TOKEN_EXCEPTION,
			INVALID_TOKEN_EXCEPTION.getHttpStatus()
		);
	}
}
