package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class IllegalWorldCupGameContentsExceptionMember extends WorldCupBaseException {
    public IllegalWorldCupGameContentsExceptionMember(String publicMessage) {
        super(
                CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS.getMessage() + " " + publicMessage,
                CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS,
                CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS.getHttpStatus()
        );
    }
}