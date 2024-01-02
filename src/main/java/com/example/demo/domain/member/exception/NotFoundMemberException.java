package com.example.demo.domain.member.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotFoundMemberException extends BaseException {

	public NotFoundMemberException() {

		super(
			NOT_FOUND_MEMBER.getMessage(),
			NOT_FOUND_MEMBER,
			NOT_FOUND_MEMBER.getHttpStatus()
		);
	}
}
