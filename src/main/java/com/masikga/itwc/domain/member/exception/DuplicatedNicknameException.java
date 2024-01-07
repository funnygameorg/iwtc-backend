package com.masikga.itwc.domain.member.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class DuplicatedNicknameException extends BaseException {
	public DuplicatedNicknameException() {
		super(
			CustomErrorCode.DUPLICATED_MEMBER_NICKNAME.getMessage(),
			CustomErrorCode.DUPLICATED_MEMBER_NICKNAME,
			CustomErrorCode.DUPLICATED_MEMBER_NICKNAME.getHttpStatus()
		);
	}
}
