package com.masikga.itwc.domain.etc.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotOwnerCommentException extends BaseException {

	public NotOwnerCommentException(Long memberId) {

		super(
			CustomErrorCode.NOT_OWNER_COMMENT.getMessage() + memberId,
			CustomErrorCode.NOT_OWNER_COMMENT,
			CustomErrorCode.NOT_OWNER_COMMENT.getHttpStatus()
		);

	}
}
