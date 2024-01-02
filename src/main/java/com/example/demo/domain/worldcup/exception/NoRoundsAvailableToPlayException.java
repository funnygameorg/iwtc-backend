package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NoRoundsAvailableToPlayException extends BaseException {
	public NoRoundsAvailableToPlayException(String publicMessage) {
		super(
			NO_ROUNDS_AVAILABLE_TO_PLAY.getMessage() + " " + publicMessage,
			NO_ROUNDS_AVAILABLE_TO_PLAY,
			NO_ROUNDS_AVAILABLE_TO_PLAY.getHttpStatus()
		);
	}
}
