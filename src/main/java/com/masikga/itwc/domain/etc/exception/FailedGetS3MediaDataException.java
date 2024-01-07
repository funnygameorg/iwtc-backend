package com.masikga.itwc.domain.etc.exception;

import com.masikga.itwc.common.error.CustomErrorCode;
import com.masikga.itwc.common.error.exception.BaseException;

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
