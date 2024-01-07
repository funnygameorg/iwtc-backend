package com.masikga.itwc.domain.worldcup.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NoRoundsAvailableToPlayException extends BaseException {
	public NoRoundsAvailableToPlayException(String publicMessage) {
		super(
			CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY.getMessage() + " " + publicMessage,
			CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY,
			CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY.getHttpStatus()
		);
	}
}
