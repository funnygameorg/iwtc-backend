package com.masikga.itwc.domain.member.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class DuplicatedServiceIdException extends BaseException {
	public DuplicatedServiceIdException() {

		super(
			CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID.getMessage(),
			CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID,
			CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID.getHttpStatus()
		);
	}
}
