package com.example.demo.domain.member.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class DuplicatedServiceIdException extends BaseException {
	public DuplicatedServiceIdException() {

		super(
			DUPLICATED_MEMBER_SERVICE_ID.getMessage(),
			DUPLICATED_MEMBER_SERVICE_ID,
			DUPLICATED_MEMBER_SERVICE_ID.getHttpStatus()
		);
	}
}
