package com.masikga.itwc.domain.worldcup.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotOwnerGameException extends BaseException {
	public NotOwnerGameException() {
		super(
			CustomErrorCode.NOT_OWNER_GAME.getMessage(),
			CustomErrorCode.NOT_OWNER_GAME,
			CustomErrorCode.NOT_OWNER_GAME.getHttpStatus()
		);
	}
}
