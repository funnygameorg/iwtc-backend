package com.example.demo.domain.member.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class DuplicatedNicknameException extends BaseException {
	public DuplicatedNicknameException() {
		super(
			DUPLICATED_MEMBER_NICKNAME.getMessage(),
			DUPLICATED_MEMBER_NICKNAME,
			DUPLICATED_MEMBER_NICKNAME.getHttpStatus()
		);
	}
}
