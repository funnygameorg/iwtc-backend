package com.masikga.itwc.domain.worldcup.exception;

import com.masikga.itwc.common.error.exception.BaseException;
import com.masikga.itwc.common.error.CustomErrorCode;

public class NotFoundWorldCupContentsException extends BaseException {
	public NotFoundWorldCupContentsException(String publicMessage) {
		super(
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + publicMessage,
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
		);
	}

	public NotFoundWorldCupContentsException(long worldCupContentsId) {
		super(
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + worldCupContentsId,
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
			CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
		);
	}
}
