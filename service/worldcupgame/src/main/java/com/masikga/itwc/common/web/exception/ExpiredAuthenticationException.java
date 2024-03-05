package com.masikga.itwc.common.web.exception;

import com.masikga.itwc.common.error.CustomErrorCode;
import com.masikga.itwc.common.error.exception.BaseException;

public class ExpiredAuthenticationException extends BaseException {
	public ExpiredAuthenticationException() {
		super(
			CustomErrorCode.EXPIRED_REMEMBER_ME.getMessage(),
			CustomErrorCode.EXPIRED_REMEMBER_ME,
			CustomErrorCode.EXPIRED_REMEMBER_ME.getHttpStatus()
		);
	}
}
