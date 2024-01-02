package com.example.demo.common.error.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

public class NotNullArgumentException extends BaseException {
	public NotNullArgumentException(Object... arguments) {
		super(
			NOT_NULL_ARGUMENT.getMessage() + arguments,
			NOT_NULL_ARGUMENT,
			NOT_NULL_ARGUMENT.getHttpStatus()
		);
	}
}
