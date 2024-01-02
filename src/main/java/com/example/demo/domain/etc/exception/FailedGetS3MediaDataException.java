package com.example.demo.domain.etc.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;

public class FailedGetS3MediaDataException extends BaseException {

	public FailedGetS3MediaDataException(
		String objectKey,
		CustomErrorCode errorCode
	) {
		super(
			errorCode.getMessage() + objectKey,
			errorCode,
			errorCode.getHttpStatus()
		);
	}

}
