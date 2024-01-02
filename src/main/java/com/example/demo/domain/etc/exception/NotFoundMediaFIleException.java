package com.example.demo.domain.etc.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotFoundMediaFIleException extends BaseException {

	public NotFoundMediaFIleException(long mediaFileId) {
		super(
			NOT_FOUND_MEDIA_FILE.getMessage() + mediaFileId,
			NOT_FOUND_MEDIA_FILE,
			NOT_FOUND_MEDIA_FILE.getHttpStatus()
		);
	}

}
