package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class NotFoundWorldCupContentsException extends BaseException {
	public NotFoundWorldCupContentsException(String publicMessage) {
		super(
			NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + publicMessage,
			NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
			NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
		);
	}

	public NotFoundWorldCupContentsException(long worldCupContentsId) {
		super(
			NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + worldCupContentsId,
			NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
			NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
		);
	}
}
