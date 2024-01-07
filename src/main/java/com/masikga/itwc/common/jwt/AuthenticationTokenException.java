package com.masikga.itwc.common.jwt;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class AuthenticationTokenException extends BaseException {
	public AuthenticationTokenException(String message) {
		super(
			CustomErrorCode.INVALID_TOKEN_EXCEPTION.getMessage() + " " + message,
			CustomErrorCode.INVALID_TOKEN_EXCEPTION,
			CustomErrorCode.INVALID_TOKEN_EXCEPTION.getHttpStatus()
		);
	}
}
