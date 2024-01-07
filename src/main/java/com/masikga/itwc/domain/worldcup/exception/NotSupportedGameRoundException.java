package com.masikga.itwc.domain.worldcup.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotSupportedGameRoundException extends BaseException {
	public NotSupportedGameRoundException(String publicMessage) {
		super(
			CustomErrorCode.NOT_SUPPORTED_GAME_ROUND.getMessage() + " " + publicMessage,
			CustomErrorCode.NOT_SUPPORTED_GAME_ROUND,
			CustomErrorCode.NOT_SUPPORTED_GAME_ROUND.getHttpStatus()
		);
	}
}
