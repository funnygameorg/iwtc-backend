package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import java.util.Objects;

import com.example.demo.common.error.exception.BaseException;

public class NotFoundWorldCupGameException extends BaseException {

	public NotFoundWorldCupGameException(String publicMessage) {
		super(
			NOT_FOUND_WORLD_CUP_GAME.getMessage() + " " + publicMessage,
			NOT_FOUND_WORLD_CUP_GAME,
			NOT_FOUND_WORLD_CUP_GAME.getHttpStatus());
	}

	public NotFoundWorldCupGameException(Long worldCupId) {
		this(Objects.toString(worldCupId));
	}
}
