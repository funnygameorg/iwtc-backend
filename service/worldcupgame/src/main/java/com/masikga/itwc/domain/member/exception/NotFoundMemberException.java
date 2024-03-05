package com.masikga.itwc.domain.member.exception;

import com.masikga.itwc.common.error.CustomErrorCode;
import com.masikga.itwc.common.error.exception.BaseException;

public class NotFoundMemberException extends BaseException {

	public NotFoundMemberException() {

		super(
			CustomErrorCode.NOT_FOUND_MEMBER.getMessage(),
			CustomErrorCode.NOT_FOUND_MEMBER,
			CustomErrorCode.NOT_FOUND_MEMBER.getHttpStatus()
		);
	}
}
