package com.example.demo.domain.worldcup.exception;

import static com.example.demo.common.error.CustomErrorCode.*;

import com.example.demo.common.error.exception.BaseException;

public class DuplicatedWorldCupGameTitleException extends BaseException {

	public DuplicatedWorldCupGameTitleException(String titleName) {

		super(
			DUPLICATED_WORLD_CUP_GAME_TITLE.getMessage() + " " + titleName,
			DUPLICATED_WORLD_CUP_GAME_TITLE,
			DUPLICATED_WORLD_CUP_GAME_TITLE.getHttpStatus()
		);
	}
}
