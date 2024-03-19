package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NotOwnerGameExceptionMember extends WorldCupBaseException {
    public NotOwnerGameExceptionMember() {
        super(
                CustomErrorCode.NOT_OWNER_GAME.getMessage(),
                CustomErrorCode.NOT_OWNER_GAME,
                CustomErrorCode.NOT_OWNER_GAME.getHttpStatus()
        );
    }
}
