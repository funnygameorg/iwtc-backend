package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NotSupportedGameRoundExceptionMember extends WorldCupBaseException {
    public NotSupportedGameRoundExceptionMember(String publicMessage) {
        super(
                CustomErrorCode.NOT_SUPPORTED_GAME_ROUND.getMessage() + " " + publicMessage,
                CustomErrorCode.NOT_SUPPORTED_GAME_ROUND,
                CustomErrorCode.NOT_SUPPORTED_GAME_ROUND.getHttpStatus()
        );
    }
}
