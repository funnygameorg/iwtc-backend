package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotOwnerGameException extends BaseException {
	public NotOwnerGameException() {
		super(
			NOT_OWNER_GAME.getMessage(),
			NOT_OWNER_GAME,
			NOT_OWNER_GAME.getHttpStatus()
		);
	}
}
