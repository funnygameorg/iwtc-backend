package com.masikga.itwc.domain.etc.exception;

import com.masikga.itwc.common.error.exception.BaseException;

public class NotSupportedFileExtensionException extends BaseException {

	public NotSupportedFileExtensionException(String requestType) {
		super(requestType, null, null);
	}
}
