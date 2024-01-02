package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class IllegalWorldCupGameContentsException extends BaseException {
	public IllegalWorldCupGameContentsException(String publicMessage) {
		super(
			ILLEGAL_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + publicMessage,
			ILLEGAL_WORLD_CUP_GAME_CONTENTS,
			ILLEGAL_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
		);
	}
}