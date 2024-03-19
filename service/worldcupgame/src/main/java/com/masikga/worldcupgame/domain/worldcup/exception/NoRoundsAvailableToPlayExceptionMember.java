package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NoRoundsAvailableToPlayExceptionMember extends WorldCupBaseException {
    public NoRoundsAvailableToPlayExceptionMember(String publicMessage) {
        super(
                CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY.getMessage() + " " + publicMessage,
                CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY,
                CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY.getHttpStatus()
        );
    }
}
