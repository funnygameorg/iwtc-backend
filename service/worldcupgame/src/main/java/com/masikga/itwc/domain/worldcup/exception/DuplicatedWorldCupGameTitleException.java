package com.masikga.itwc.domain.worldcup.exception;

import com.masikga.itwc.common.error.CustomErrorCode;
import com.masikga.itwc.common.error.exception.BaseException;

public class DuplicatedWorldCupGameTitleException extends BaseException {

	public DuplicatedWorldCupGameTitleException(String titleName) {

		super(
			CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE.getMessage() + " " + titleName,
			CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE,
			CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE.getHttpStatus()
		);
	}
}
