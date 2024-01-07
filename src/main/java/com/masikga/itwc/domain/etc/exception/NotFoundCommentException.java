package com.masikga.itwc.domain.etc.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotFoundCommentException extends BaseException {
	public NotFoundCommentException(Long commentId) {
		super(
			CustomErrorCode.NOT_FOUND_COMMENT.getMessage() + commentId,
			CustomErrorCode.NOT_FOUND_COMMENT,
			CustomErrorCode.NOT_FOUND_COMMENT.getHttpStatus()
		);
	}
}
