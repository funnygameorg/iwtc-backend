package com.example.demo.domain.etc.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotOwnerCommentException extends BaseException {

	public NotOwnerCommentException(Long memberId) {

		super(
			NOT_OWNER_COMMENT.getMessage() + memberId,
			NOT_OWNER_COMMENT,
			NOT_OWNER_COMMENT.getHttpStatus()
		);

	}
}
