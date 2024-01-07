package com.masikga.itwc.domain.worldcup.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class IllegalWorldCupGameContentsException extends BaseException {
	public IllegalWorldCupGameContentsException(String publicMessage) {
		super(
			CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + publicMessage,
			CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS,
			CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
		);
	}
}