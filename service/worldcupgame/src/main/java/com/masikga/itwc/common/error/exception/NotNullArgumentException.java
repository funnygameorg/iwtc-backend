package com.masikga.itwc.common.error.exception;

import com.masikga.itwc.common.error.CustomErrorCode;

public class NotNullArgumentException extends BaseException {
	public NotNullArgumentException(Object... arguments) {
		super(
			CustomErrorCode.NOT_NULL_ARGUMENT.getMessage() + arguments,
			CustomErrorCode.NOT_NULL_ARGUMENT,
			CustomErrorCode.NOT_NULL_ARGUMENT.getHttpStatus()
		);
	}
}
