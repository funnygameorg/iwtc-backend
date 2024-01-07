package com.masikga.itwc.common.web.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class RequestWithBlackListedAccessToken extends BaseException {
	public RequestWithBlackListedAccessToken(String accessToken) {
		super(
			CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN.getMessage() + " " + accessToken,
			CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN,
			CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN.getHttpStatus()
		);

	}
}
