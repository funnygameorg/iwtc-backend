package com.masikga.itwc.domain.worldcup.exception;

import java.util.Objects;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotFoundWorldCupGameException extends BaseException {

	public NotFoundWorldCupGameException(String publicMessage) {
		super(
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME.getMessage() + " " + publicMessage,
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME,
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME.getHttpStatus());
	}

	public NotFoundWorldCupGameException(Long worldCupId) {
		this(Objects.toString(worldCupId));
	}
}
