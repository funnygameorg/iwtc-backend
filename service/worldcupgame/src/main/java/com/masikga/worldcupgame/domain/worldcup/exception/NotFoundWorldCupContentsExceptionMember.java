package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NotFoundWorldCupContentsExceptionMember extends WorldCupBaseException {
    public NotFoundWorldCupContentsExceptionMember(String publicMessage) {
        super(
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + publicMessage,
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
        );
    }

    public NotFoundWorldCupContentsExceptionMember(long worldCupContentsId) {
        super(
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + worldCupContentsId,
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
        );
    }
}
