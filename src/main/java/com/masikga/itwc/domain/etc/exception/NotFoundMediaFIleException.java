package com.masikga.itwc.domain.etc.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotFoundMediaFIleException extends BaseException {

	public NotFoundMediaFIleException(long mediaFileId) {
		super(
			CustomErrorCode.NOT_FOUND_MEDIA_FILE.getMessage() + mediaFileId,
			CustomErrorCode.NOT_FOUND_MEDIA_FILE,
			CustomErrorCode.NOT_FOUND_MEDIA_FILE.getHttpStatus()
		);
	}

}
