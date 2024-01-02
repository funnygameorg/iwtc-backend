package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotSupportedGameRoundException extends BaseException {
	public NotSupportedGameRoundException(String publicMessage) {
		super(
			NOT_SUPPORTED_GAME_ROUND.getMessage() + " " + publicMessage,
			NOT_SUPPORTED_GAME_ROUND,
			NOT_SUPPORTED_GAME_ROUND.getHttpStatus()
		);
	}
}
