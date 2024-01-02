package com.example.demo.domain.etc.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotFoundCommentException extends BaseException {
	public NotFoundCommentException(Long commentId) {
		super(
			NOT_FOUND_COMMENT.getMessage() + commentId,
			NOT_FOUND_COMMENT,
			NOT_FOUND_COMMENT.getHttpStatus()
		);
	}
}
