package com.example.demo.common.jpa;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotFoundDataInRequestException extends BaseException {
	public NotFoundDataInRequestException() {
		super(
			NOT_FOUND_DATA_IN_REQUEST.getMessage(),
			NOT_FOUND_DATA_IN_REQUEST,
			NOT_FOUND_DATA_IN_REQUEST.getHttpStatus()
		);
	}
}
