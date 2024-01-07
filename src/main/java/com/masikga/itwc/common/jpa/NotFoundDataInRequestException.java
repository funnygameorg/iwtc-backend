package com.masikga.itwc.common.jpa;

import static com.masikga.itwc.common.error.CustomErrorCode.*;

import com.masikga.itwc.common.error.exception.BaseException;

public class NotFoundDataInRequestException extends BaseException {
	public NotFoundDataInRequestException() {
		super(
			NOT_FOUND_DATA_IN_REQUEST.getMessage(),
			NOT_FOUND_DATA_IN_REQUEST,
			NOT_FOUND_DATA_IN_REQUEST.getHttpStatus()
		);
	}
}
